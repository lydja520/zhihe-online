package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Coupon;
import cn.com.zhihetech.online.bean.CouponItem;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
public interface ICouponService extends SupportService<Coupon> {
    /**
     * 判断优惠券信息是否可编辑
     *
     * @param coupon
     * @return
     */
    boolean isEditAble(Coupon coupon);

    /**
     * 修改优惠券的基本信息
     *
     * @param coupon
     */
    void updateBaseInfo(Coupon coupon,Date beginValidity, Date validity);

    void addCoupon(Coupon coupon, Date beginValidity, Date validity);

    ResponseMessage updateCouponItem(String couponId, String userId);

    //TODO:得到每一个商家的奖券
    Map<String,List<Coupon>> getActivityCoupon(Pager pager, List<Merchant> merchants, String activityId);
}
