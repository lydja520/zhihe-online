package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.AppVersion;
import cn.com.zhihetech.online.dao.IAppVersionDao;
import cn.com.zhihetech.online.service.IAppVersionService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
@Service("appVersionService")
public class AppVersionServiceImpl implements IAppVersionService {

    @Resource(name = "appVersionDao")
    private IAppVersionDao appVersionDao;

    @Override
    public AppVersion getById(String id) {
        return this.appVersionDao.findEntityById(id);
    }

    @Override
    public void delete(AppVersion appVersion) {
        this.appVersionDao.deleteEntity(appVersion);
    }

    @Override
    public AppVersion add(AppVersion appVersion) {
        return this.appVersionDao.saveEntity(appVersion);
    }

    @Override
    public void update(AppVersion appVersion) {
        this.appVersionDao.updateEntity(appVersion);
    }

    @Override
    public List<AppVersion> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.appVersionDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<AppVersion> getPageData(Pager pager, IQueryParams queryParams) {
        return this.appVersionDao.getPageData(pager, queryParams);
    }

    @Override
    public AppVersion getLastVersion() {
        Pager pager = new Pager(1, 1);
        IQueryParams queryParams = new GeneralQueryParams().sort("versionCode", Order.DESC);
        List<AppVersion> versions = this.appVersionDao.getEntities(pager, queryParams);
        if (versions.size() > 0) {
            return versions.get(0);
        }
        return null;
    }
}
