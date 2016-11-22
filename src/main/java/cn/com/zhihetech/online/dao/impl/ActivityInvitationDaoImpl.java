package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ActivityInvitation;
import cn.com.zhihetech.online.dao.IActivityInvitationDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/3/21.
 */
@Repository("activityInvitationDao")
public class ActivityInvitationDaoImpl extends SimpleSupportDao<ActivityInvitation> implements IActivityInvitationDao {
}
