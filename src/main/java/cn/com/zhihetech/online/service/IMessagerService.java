package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Messager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by YangDaiChun on 2015/12/18.
 */
public interface IMessagerService extends SupportService<Messager> {

    void resetPassword(String phoneNumber, String securityCode);

    /**
     * 发送注册验证码
     *
     * @param phoneNumber
     */
    void executeSendRegisterSecurityCode(String phoneNumber);

    /**
     * 发送找回密码验证码
     *
     * @param phoneNumber
     */
    void executeSendAlterPwdSecurityCode(String phoneNumber);

    /**
     * 发送提现验证码
     *
     * @param phoneNumber
     */
    void executeSendWithDrawMoneySecurityCode(String phoneNumber);
}
