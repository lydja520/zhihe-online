package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ActivityOrder;
import cn.com.zhihetech.online.dao.IActivityOrderDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ydc on 16-5-30.
 */
@Repository(value = "activityOrderDao")
public class ActivityOrderDaoImpl extends SimpleSupportDao<ActivityOrder> implements IActivityOrderDao {
}
