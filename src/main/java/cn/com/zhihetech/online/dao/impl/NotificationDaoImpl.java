package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Notification;
import cn.com.zhihetech.online.dao.INotificationDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/3/14.
 */

@Repository(value = "notificationDao")
public class NotificationDaoImpl extends SimpleSupportDao<Notification> implements INotificationDao{
}
