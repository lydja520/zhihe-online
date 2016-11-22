package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Notification;
import cn.com.zhihetech.online.dao.INotificationDao;
import cn.com.zhihetech.online.service.INotificationService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/3/14.
 */
@Service(value = "notificationService")
public class NotificationServiceImpl implements INotificationService{

    @Resource(name = "notificationDao")
    private INotificationDao notificationDao;

    @Override
    public Notification getById(String id) {
        return null;
    }

    @Override
    public void delete(Notification notification) {

    }

    @Override
    public Notification add(Notification notification) {
        return null;
    }

    @Override
    public void update(Notification notification) {

    }

    @Override
    public List<Notification> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<Notification> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }
}
