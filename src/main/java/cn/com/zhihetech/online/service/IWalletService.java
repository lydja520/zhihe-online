package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Wallet;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by YangDaiChun on 2016/3/4.
 */
public interface IWalletService extends SupportService<Wallet>{

    ResponseMessage executePutRedEnvelopItemMoneyToWallet(String userId, String redEnvelopIdItemId);

    List<Object> getTotalMoney(String totalMoney,IQueryParams queryParams);
}
