package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Messager;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.bean.UserWithdraw;
import cn.com.zhihetech.online.bean.Wallet;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IMessagerDao;
import cn.com.zhihetech.online.dao.IUserDao;
import cn.com.zhihetech.online.dao.IUserWithdrawDao;
import cn.com.zhihetech.online.dao.IWalletDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IUserWithdrawServie;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/3/12.
 */
@Service(value = "userWithdrawService")
public class UserWithdrawServieImpl implements IUserWithdrawServie {

    @Resource(name = "userWithdrawDao")
    private IUserWithdrawDao userWithdrawDao;
    @Resource(name = "userDao")
    private IUserDao userDao;
    @Resource(name = "messaerDao")
    private IMessagerDao messagerDao;
    @Resource(name = "walletDao")
    private IWalletDao walletDao;

    @Override
    public UserWithdraw getById(String id) {
        return this.userWithdrawDao.findEntityById(id);
    }

    @Override
    public void delete(UserWithdraw userWithdraw) {

    }

    @Override
    public UserWithdraw add(UserWithdraw userWithdraw) {
        return null;
    }

    @Override
    public void update(UserWithdraw userWithdraw) {
        this.userWithdrawDao.updateEntity(userWithdraw);
    }

    @Override
    public List<UserWithdraw> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<UserWithdraw> getPageData(Pager pager, IQueryParams queryParams) {
        return this.userWithdrawDao.getPageData(pager, queryParams);
    }

    @Override
    public float addApplyWithdraw(String userId, float money, String securityCode, String aliCode) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("userId", userId);
        List<Object> phoneNumbers = this.userDao.getProperty("userPhone", null, queryParams);
        if (phoneNumbers.size() == 0) {
            throw new SystemException("找不到该用户");
        }
        String phoneNumber = (String) phoneNumbers.get(0);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("phoneNumber", phoneNumber)
                .andEqual("scState", Constant.SECURITY_WITHDRAW_MONEY)
                .andEqual("securityCode", securityCode);
        List<Messager> messagers = this.messagerDao.getEntities(null, queryParams);
        if (messagers.size() == 0) {
            throw new SystemException("验证码错误！");
        }
        Date validity = messagers.get(0).getValidity();
        if (new Date().after(validity)) {
            throw new SystemException("验证码错误！");
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("user.userId", userId);
        List<Wallet> wallets = this.walletDao.getEntities(queryParams);
        if (wallets.size() == 0) {
            throw new SystemException("提现失败，用户余额未达到指定数额，不支持提现");
        }
        Wallet wallet = wallets.get(0);
        float surplus = wallet.getTotalMoney();
        if (surplus <= 0) {
            throw new SystemException("余额不足，不支持提现");
        }
        surplus = surplus - money;
        if (surplus < 0) {
            throw new SystemException("提现失败，用户余额未达到指定数额，不支持提现");
        }
        wallet.setTotalMoney(surplus);
        this.walletDao.updateEntity(wallet);
        UserWithdraw userWithdraw = new UserWithdraw();
        User user = new User();
        user.setUserId(userId);
        userWithdraw.setUser(user);
        userWithdraw.setAliCode(aliCode);
        userWithdraw.setOperator(null);
        userWithdraw.setApplyDate(new Date());
        userWithdraw.setMoney(money);
        float poundage = new BigDecimal(money * Constant.MERCHANT_BILL_POUNDAGE_RATE).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        userWithdraw.setPoundage(poundage);
        float realMoney = new BigDecimal(money - poundage).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        userWithdraw.setRealMoney(realMoney);
        userWithdraw.setWithdrawDate(null);
        userWithdraw.setWithdrawState(Constant.APPLY_WITHDRAW);
        this.userWithdrawDao.saveEntity(userWithdraw);
        return surplus;
    }

    @Override
    public void executeWithDrawFilure(String withDrawId, String withDrawFailureReason) {
        UserWithdraw userWithdraw = this.userWithdrawDao.findEntityById(withDrawId);
        if (userWithdraw == null) {
            throw new SystemException("查不到对应的记录");
        }
        if (userWithdraw.getWithdrawState() == Constant.WITHDRAW_ERR) {
            throw new SystemException("该条数据已经处理过，请勿重复处理");
        }
        userWithdraw.setWithdrawState(Constant.WITHDRAW_ERR);
        User user = userWithdraw.getUser();
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("user", user);
        List<Wallet> wallets = this.walletDao.getEntities(queryParams);
        if (wallets.size() <= 0) {
            throw new SystemException("用户对应的红包还未生成！");
        }
        Wallet wallet = wallets.get(0);
        wallet.setTotalMoney(wallet.getTotalMoney() + userWithdraw.getMoney());
        this.walletDao.updateEntity(wallet);
        this.userWithdrawDao.updateEntity(userWithdraw);
    }

}
