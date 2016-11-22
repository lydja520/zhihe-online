package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by YangDaiChun on 2016/1/25.
 */
public interface IReceivedGoodsAddressService extends SupportService<ReceivedGoodsAddress> {

    ReceivedGoodsAddress saveOrUpdate(ReceivedGoodsAddress receivedGoodsAddress);
}
