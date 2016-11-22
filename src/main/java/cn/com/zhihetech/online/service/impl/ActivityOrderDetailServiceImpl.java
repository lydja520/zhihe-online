package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ActivityOrderDetail;
import cn.com.zhihetech.online.dao.IActivityOrderDetailDao;
import cn.com.zhihetech.online.service.IActivityOrderDetailService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ydc on 16-5-30.
 */
@Service(value = "acitivityOrderDetailService")
public class ActivityOrderDetailServiceImpl implements IActivityOrderDetailService {

    @Resource(name = "activityOrderDetailDao")
    private IActivityOrderDetailDao activityOrderDetailDao;

    @Override
    public ActivityOrderDetail getById(String id) {
        return null;
    }

    @Override
    public void delete(ActivityOrderDetail activityOrderDetail) {

    }

    @Override
    public ActivityOrderDetail add(ActivityOrderDetail activityOrderDetail) {
        return null;
    }

    @Override
    public void update(ActivityOrderDetail activityOrderDetail) {

    }

    @Override
    public List<ActivityOrderDetail> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.activityOrderDetailDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<ActivityOrderDetail> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public List<Object> getProperty(String selector, IQueryParams queryParams, Pager pager) {
        return this.activityOrderDetailDao.getProperty(selector, pager, queryParams);
    }
}
