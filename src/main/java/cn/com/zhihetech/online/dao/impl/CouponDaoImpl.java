package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Coupon;
import cn.com.zhihetech.online.dao.ICouponDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
@Repository("couponDao")
public class CouponDaoImpl extends SimpleSupportDao<Coupon> implements ICouponDao {
}
