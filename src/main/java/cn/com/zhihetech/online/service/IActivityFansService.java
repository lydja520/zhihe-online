package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ActivityFans;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
public interface IActivityFansService extends SupportService<ActivityFans> {

    PageData<Object> getPageDataProperty(String property, Pager pager, IQueryParams queryParams);

    //TODO:得到每一个商家所邀请的所有会员
    public Map<String, List<ActivityFans>> getFansUser(Pager pager, List<Merchant> merchants, String activityId);
}
