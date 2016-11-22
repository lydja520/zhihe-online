package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ActivityOrderDetail;
import cn.com.zhihetech.online.dao.IActivityOrderDetailDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ydc on 16-5-30.
 */
@Repository(value = "activityOrderDetailDao")
public class ActivityOrderDetailDaoImpl extends SimpleSupportDao<ActivityOrderDetail> implements IActivityOrderDetailDao {
}
