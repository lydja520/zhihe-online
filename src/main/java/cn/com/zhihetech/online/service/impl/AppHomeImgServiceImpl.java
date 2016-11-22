package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.AppHomeImg;
import cn.com.zhihetech.online.dao.IAppHomeImgDao;
import cn.com.zhihetech.online.service.IAppHomeImgService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
@Service(value = "appHomeImgService")
public class AppHomeImgServiceImpl implements IAppHomeImgService {

    @Resource(name = "appHomeImgDao")
    private IAppHomeImgDao appHomeImgDao;

    @Override
    public AppHomeImg getById(String id) {
        return null;
    }

    @Override
    public void delete(AppHomeImg appHomeImg) {

    }

    @Override
    public AppHomeImg add(AppHomeImg appHomeImg) {
        return null;
    }

    @Override
    public void update(AppHomeImg appHomeImg) {

    }

    @Override
    public List<AppHomeImg> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<AppHomeImg> getPageData(Pager pager, IQueryParams queryParams) {
        return this.appHomeImgDao.getPageData(pager,queryParams);
    }

    @Override
    public void addOrUpdateImg(AppHomeImg appHomeImg) {
        List<AppHomeImg> appHomeImgs = this.appHomeImgDao.getEntities(null,null);
        if(appHomeImgs.size() == 0){
            this.appHomeImgDao.saveEntity(appHomeImg);
            return;
        }
        AppHomeImg _appHomeImg = appHomeImgs.get(0);
        _appHomeImg.setHomeImgName(appHomeImg.getHomeImgName());
        _appHomeImg.setImgInfo(appHomeImg.getImgInfo());
        this.appHomeImgDao.updateEntity(_appHomeImg);
    }
}
