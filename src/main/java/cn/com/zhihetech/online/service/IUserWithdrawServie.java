package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.UserWithdraw;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by YangDaiChun on 2016/3/12.
 */
public interface IUserWithdrawServie extends SupportService<UserWithdraw>{

    float addApplyWithdraw(String userId,float money,String securityCode,String aliCode);

    void executeWithDrawFilure(String withDrawId,String withDrawFailureReason);
}
