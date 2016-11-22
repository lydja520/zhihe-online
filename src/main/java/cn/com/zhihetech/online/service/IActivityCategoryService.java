package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ActivityCategory;
import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ShenYunjie on 2015/12/4.
 */
public interface IActivityCategoryService extends SupportService<ActivityCategory> {
    ActivityCategory saveOrUpdate(ActivityCategory activityCategory);

    /**
     * 根据用户查询此用户可以参加的活动类别
     * @param admin
     * @return
     */
    List<ActivityCategory> getCategsByAdmin(Admin admin);
}
