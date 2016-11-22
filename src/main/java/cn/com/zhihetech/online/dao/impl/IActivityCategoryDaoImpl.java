package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ActivityCategory;
import cn.com.zhihetech.online.dao.IActivityCategoryDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2015/12/4.
 */
@Repository("activityCategoryDao")
public class IActivityCategoryDaoImpl extends SimpleSupportDao<ActivityCategory> implements IActivityCategoryDao {
}
