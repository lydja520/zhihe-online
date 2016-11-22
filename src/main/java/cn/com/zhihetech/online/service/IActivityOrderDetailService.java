package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ActivityOrderDetail;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ydc on 16-5-30.
 */
public interface IActivityOrderDetailService extends SupportService<ActivityOrderDetail> {
    List<Object> getProperty(String selector, IQueryParams queryParams, Pager pager);
}
