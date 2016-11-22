package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ShoppingCenter;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by YangDaiChun on 2016/3/10.
 */
public interface IShoppingCenterService extends SupportService<ShoppingCenter> {

    void updateDeleteState(String scId,boolean permit);
}
