package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.CouponItem;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by YangDaiChun on 2016/3/9.
 */
public interface ICouponItemService extends SupportService<CouponItem> {

    CouponItem executeUseCoupon(Admin admin, String couponCode, String userPhone);
}
