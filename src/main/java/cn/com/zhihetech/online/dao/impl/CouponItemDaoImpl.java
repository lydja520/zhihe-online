package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.CouponItem;
import cn.com.zhihetech.online.dao.ICouponItemDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/3/9.
 */
@Repository(value = "couponItemDao")
public class CouponItemDaoImpl extends SimpleSupportDao<CouponItem> implements ICouponItemDao{
}
