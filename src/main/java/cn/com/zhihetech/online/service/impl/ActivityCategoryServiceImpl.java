package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ActivityCategory;
import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.dao.IActivityCategoryDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityCategoryService;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/12/4.
 */
@Service("activityCategoryService")
public class ActivityCategoryServiceImpl implements IActivityCategoryService {

    @Resource(name = "activityCategoryDao")
    private IActivityCategoryDao activityCategoryDao;
    @Resource(name="adminService")
    private IAdminService adminService;

    @Override
    public ActivityCategory saveOrUpdate(ActivityCategory activityCategory) {
        return this.activityCategoryDao.saveOrUpdate(activityCategory);
    }

    @Override
    public List<ActivityCategory> getCategsByAdmin(Admin admin) {
        if(admin == null){
            throw new SystemException("admin not able null");
        }
        admin = this.adminService.getById(admin.getAdminId());
        IQueryParams queryParams = new GeneralQueryParams();
        if(!admin.isPermit()){
            throw new SystemException("admin have disable");    //用户已禁用
        }
        if(!admin.isOfficial()){
            queryParams.andEqual("official",false);
        }
        return this.getAllByParams(null,queryParams);
    }

    @Override
    public ActivityCategory getById(String id) {
        return this.activityCategoryDao.findEntityById(id);
    }

    @Override
    public void delete(ActivityCategory activityCategory) {
        this.activityCategoryDao.deleteEntity(activityCategory);
    }

    @Override
    public ActivityCategory add(ActivityCategory activityCategory) {
        return this.activityCategoryDao.saveEntity(activityCategory);
    }

    @Override
    public void update(ActivityCategory activityCategory) {
        this.activityCategoryDao.updateEntity(activityCategory);
    }

    @Override
    public List<ActivityCategory> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.activityCategoryDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<ActivityCategory> getPageData(Pager pager, IQueryParams queryParams) {
        return this.activityCategoryDao.getPageData(pager, queryParams);
    }
}
