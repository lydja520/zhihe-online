package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ActivityFans;
import cn.com.zhihetech.online.dao.IActivityFansDao;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
@Repository("activityFansDao")
public class ActivityFansDaoImpl extends SimpleSupportDao<ActivityFans> implements IActivityFansDao {

}
