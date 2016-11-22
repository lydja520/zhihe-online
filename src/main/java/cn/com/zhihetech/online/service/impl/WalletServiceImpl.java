package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IWalletService;
import cn.com.zhihetech.online.util.NumberUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/3/4.
 */
@Service(value = "walletService")
public class WalletServiceImpl implements IWalletService {
    @Resource(name = "walletDao")
    private IWalletDao walletDao;
    @Resource(name = "userDao")
    private IUserDao userDao;
    @Resource(name = "redEnvelopItemDao")
    private IRedEnvelopItemServiceDao redEnvelopItemServiceDao;
    @Resource(name = "redEnvelopDao")
    private IRedEnvelopDao redEnvelopDao;
    @Resource(name = "focusMerchantDao")
    private IFocusMerchantDao focusMerchantDao;

    @Override
    public Wallet getById(String id) {
        return this.walletDao.findEntityById(id);
    }

    @Override
    public void delete(Wallet wallet) {

    }

    @Override
    public Wallet add(Wallet wallet) {
        return this.walletDao.saveEntity(wallet);
    }

    @Override
    public void update(Wallet wallet) {
        this.walletDao.updateEntity(wallet);
    }

    @Override
    public List<Wallet> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.walletDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<Wallet> getPageData(Pager pager, IQueryParams queryParams) {
        return this.walletDao.getPageData(pager, queryParams);
    }

    /**
     * 把红包里的钱存入用户钱包
     *
     * @param userId
     * @param redEnvelopItemId
     * @return
     */
    @Override
    public ResponseMessage executePutRedEnvelopItemMoneyToWallet(String userId, String redEnvelopItemId) {
         /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓判断根据用户id查出的用户是否是合法用户↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("userId", userId);
        long totalRecord = this.userDao.getRecordTotal(queryParams);
        if (totalRecord <= 0) {
            throw new SystemException("你还不是在实淘上注册的用户，请先注册实淘用户！");
        }
        User user = new User(userId);
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑判断根据用户id查出的用户是否是合法用户↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓判断红包是否支持存入钱包↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("envelopItemId", redEnvelopItemId)
                .andEqual("user.userId", userId);
        String[] selectors = new String[]{"amountOfMoney", "extractState"};
        List<Object[]> oRedEnvelopItemInfos = this.redEnvelopItemServiceDao.getProperties(selectors, null, queryParams);
        if (oRedEnvelopItemInfos == null || oRedEnvelopItemInfos.isEmpty()) {
            throw new SystemException("找不到对应的红包！");
        }
        Object[] oRedEnvelopItem = oRedEnvelopItemInfos.get(0);
        float amountOfMoney = Float.parseFloat(oRedEnvelopItem[0].toString());
        boolean extractState = Boolean.parseBoolean(oRedEnvelopItem[1].toString());
        if (extractState) {
            throw new SystemException("该红包已经存入到钱包，无需重复操作！");
        }
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑判断红包是否支持存入钱包↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓把红包的钱存入钱包↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("user.userId", userId);
        Wallet wallet = null;
        selectors = new String[]{"walletId", "totalMoney"};
        List<Object[]> walletInfos = this.walletDao.getProperties(selectors, null, queryParams);
        if (walletInfos == null || walletInfos.isEmpty()) {
            wallet = new Wallet();
            wallet.setUser(new User(userId));
            amountOfMoney = NumberUtils.floatScale(2, amountOfMoney);
            wallet.setTotalMoney(amountOfMoney);
            this.walletDao.saveEntity(wallet);
        } else {
            String walletId = (String) walletInfos.get(0)[0];
            float totalMoney = Float.parseFloat(walletInfos.get(0)[1].toString());
            totalMoney += amountOfMoney;
            totalMoney = NumberUtils.floatScale(2, totalMoney);
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("walletId", walletId);
            Map<String, Object> paramAndValue = new HashMap<>();
            paramAndValue.put("totalMoney", totalMoney);
            this.walletDao.executeUpdate(paramAndValue, queryParams);
        }
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑把红包的钱存入钱包↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓更新红包状态↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("envelopItemId", redEnvelopItemId)
                .andEqual("extractState", false);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("extractState", true);
        if (this.redEnvelopItemServiceDao.executeUpdate(paramAndValue, queryParams) < 1) {
            throw new SystemException("该红包已经存入到钱包，无需重复操作！");
        }
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑更新红包状态↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓根据红包详情Id找到发红包的商家↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("envelopItemId", redEnvelopItemId);
        List<Object> redEnvelopIds = this.redEnvelopItemServiceDao.getProperty("redEnvelop.envelopId", null, queryParams);
        if (redEnvelopIds.size() < 1) {
            throw new SystemException("系统出现异常，请与管理员联系！");
        }
        String _redEnvelopId = (String) redEnvelopIds.get(0);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("envelopId", _redEnvelopId);
        List<Object> merchantIds = this.redEnvelopDao.getProperty("merchant.merchantId", null, queryParams);
        if (merchantIds == null || merchantIds.isEmpty()) {
            throw new SystemException("系统出现异常，请与管理员联系！");
        }
        String merchantId = (String) merchantIds.get(0);
        queryParams = new GeneralQueryParams();
        Merchant merchant = new Merchant();
        merchant.setMerchantId(merchantId);
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑根据红包详情Id找到发红包的商家↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓用户未关注商家则关注商家↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        queryParams.andEqual("merchant", merchant).andEqual("user", user);
        List<FocusMerchant> focusMerchants = this.focusMerchantDao.getEntities(null, queryParams);
        if (focusMerchants == null || focusMerchants.isEmpty()) {
            FocusMerchant focusMerchant = new FocusMerchant();
            focusMerchant.setMerchant(merchant);
            focusMerchant.setUser(user);
            focusMerchant.setFocusDate(new Date());
            this.focusMerchantDao.saveEntity(focusMerchant);
        }
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑用户未关注商家则关注商家↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        ResponseMessage responseMessage = new ResponseMessage(ResponseStatusCode.SUCCESS_CODE, true, "存入钱包成功");
        responseMessage.setAttribute(null);
        return responseMessage;
    }

    @Override
    public List<Object> getTotalMoney(String totalMoney, IQueryParams queryParams) {
        return this.walletDao.getProperty(totalMoney, null, queryParams);
    }
}
