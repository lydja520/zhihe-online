package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Messager;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.WebChineseConfig;
import cn.com.zhihetech.online.dao.IMessagerDao;
import cn.com.zhihetech.online.dao.IUserDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IMessagerService;
import cn.com.zhihetech.online.util.SMSUtils;
import cn.com.zhihetech.util.common.MD5Utils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by YangDaiChun on 2015/12/18.
 */
@Service(value = "messagerService")
public class MessagerServiceImpl implements IMessagerService {

    @Resource(name = "messaerDao")
    private IMessagerDao messagerDao;
    @Resource(name = "userDao")
    private IUserDao userDao;

    @Override
    public Messager getById(String id) {
        return this.messagerDao.findEntityById(id);
    }

    @Override
    public void delete(Messager messager) {

    }

    @Override
    public Messager add(Messager messager) {
        return this.messagerDao.saveEntity(messager);
    }

    @Override
    public void update(Messager messager) {
        this.messagerDao.updateEntity(messager);
    }

    @Override
    public List<Messager> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.messagerDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<Messager> getPageData(Pager pager, IQueryParams queryParams) {
        return this.messagerDao.getPageData(pager, queryParams);
    }

    /**
     * 重置密码
     *
     * @param phoneNumber
     * @param securityCode
     */
    @Override
    public void resetPassword(String phoneNumber, String securityCode) {
        if (!executeExistByMobileNum(phoneNumber)) {
            throw new SystemException("用户还未注册！");
        }
        IQueryParams queryParams;

        queryParams = new GeneralQueryParams()
                .andEqual("phoneNumber", phoneNumber)
                .andEqual("scState", Constant.SECURITY_ALTERPWD)
                .andEqual("securityCode", securityCode)
                .andMoreAndEq("validity", new Date());
        List<Messager> messagers = this.messagerDao.getEntities(queryParams);
        if (messagers == null || messagers.size() <= 0) {
            throw new SystemException("验证码错误");
        }

        String newPwd = getRandomCharAndNumr(6);
        String smsText = MessageFormat.format(WebChineseConfig.MsgTemplate.SEND_NEW_PASS_WORD, newPwd);
        if (!SMSUtils.sendSMS(phoneNumber, smsText)) {
            throw new SystemException("找回密码失败，请重试！");
        }

        Messager msg = new Messager();
        msg.setPhoneNumber(phoneNumber);
        msg.setScState(Constant.COMMON_SMS_MESSAGE);
        msg.setSendDate(new Date());
        msg.setSecurityMsg(smsText);
        this.add(msg);

        newPwd = MD5Utils.getMD5Msg(newPwd);
        queryParams = new GeneralQueryParams()
                .andEqual("userPhone", phoneNumber);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("pwd", newPwd);
        this.userDao.executeUpdate(paramAndValue, queryParams);
    }

    @Override
    public void executeSendRegisterSecurityCode(String phoneNumber) {
        if (executeExistByMobileNum(phoneNumber)) {
            throw new SystemException("此号码已经注册请勿重复注册！");
        }
        String securityCode = this.generateSecurityCode();
        String smsText = MessageFormat.format(WebChineseConfig.MsgTemplate.REGISTER_VER_CODE, securityCode);
        sendSmsSecurityCode(phoneNumber, smsText, securityCode, Constant.SECURITY_REGISTER);
    }

    @Override
    public void executeSendAlterPwdSecurityCode(String phoneNumber) {
        if (!executeExistByMobileNum(phoneNumber)) {
            throw new SystemException("此号码还未注册！");
        }
        String securityCode = this.generateSecurityCode();
        String smsText = MessageFormat.format(WebChineseConfig.MsgTemplate.FIND_PASSWORD_VER_CODE, securityCode);
        sendSmsSecurityCode(phoneNumber, smsText, securityCode, Constant.SECURITY_ALTERPWD);
    }

    @Override
    public void executeSendWithDrawMoneySecurityCode(String phoneNumber) {
        if (!executeExistByMobileNum(phoneNumber)) {
            throw new SystemException("用户不存在！");
        }
        String securityCode = this.generateSecurityCode();
        String smsText = MessageFormat.format(WebChineseConfig.MsgTemplate.APPLY_TAKE_CASH, securityCode);
        sendSmsSecurityCode(phoneNumber, smsText, securityCode, Constant.SECURITY_WITHDRAW_MONEY);
    }

    protected void sendSmsSecurityCode(String mobileNum, String smsText, String securityCode, int securityState) {

        if (SMSUtils.sendSMS(mobileNum, smsText)) {
            Messager messager = new Messager();
            messager.setPhoneNumber(mobileNum);
            messager.setSecurityCode(securityCode);
            messager.setSecurityMsg(smsText);
            messager.setSendDate(new Date());
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MILLISECOND, WebChineseConfig.VER_CODE_VALID);
            messager.setValidity(nowTime.getTime());
            messager.setScState(securityState);
            this.add(messager);
        } else {
            throw new SystemException("发送短信失败，请重试！");
        }
    }

    private static String getRandomCharAndNumr(Integer length) {
        String str = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            /*boolean b = random.nextBoolean();
            if (b) { // 字符串
                // int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母
                str += (char) (65 + random.nextInt(26));// 取得大写字母
            } else { // 数字
                str += String.valueOf(random.nextInt(10));
            }*/
            str += String.valueOf(random.nextInt(10));
        }
        return str;
    }

    /**
     * 根据手机号判断用户是否存在
     *
     * @param mobileNum
     * @return
     */
    private boolean executeExistByMobileNum(String mobileNum) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("userPhone", mobileNum);
        List<Object> tmps = this.userDao.getProperty("userId", null, queryParams);
        if (tmps != null && tmps.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 随机生成4位验证码
     *
     * @return
     */
    protected String generateSecurityCode() {
        int number;
        while (true) {
            number = (int) (Math.random() * 10000);
            if (number >= 1000 && number < 10000) {
                break;
            }
        }
        return String.valueOf(number);
    }
}
