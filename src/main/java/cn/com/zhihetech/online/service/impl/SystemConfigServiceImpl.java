package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.SystemConfig;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IAppHomeImgDao;
import cn.com.zhihetech.online.dao.ISystemConfigDao;
import cn.com.zhihetech.online.service.IImgInfoService;
import cn.com.zhihetech.online.service.ISystemConfigService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/3/18.
 */
@Service("systemConfigService")
public class SystemConfigServiceImpl implements ISystemConfigService {

    //    @Resource(name = "systemConfigDao")
//    private ISystemConfigDao systemConfigDao;
    @Resource(name = "imgInfoService")
    private IImgInfoService imgInfoService;
    @Resource(name = "appHomeImgDao")
    private IAppHomeImgDao appHomeImgDao;
    @Resource(name = "systemConfigDao")
    private ISystemConfigDao systemConfigDao;


    @Override
    public SystemConfig getById(String id) {
        return null;
    }

    @Override
    public void delete(SystemConfig systemConfig) {

    }

    @Override
    public SystemConfig add(SystemConfig systemConfig) {
        return this.systemConfigDao.saveEntity(systemConfig);
    }

    @Override
    public void update(SystemConfig systemConfig) {

    }

    @Override
    public List<SystemConfig> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.systemConfigDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<SystemConfig> getPageData(Pager pager, IQueryParams queryParams) {
        return this.systemConfigDao.getPageData(pager, queryParams);
    }

    @Override
    public ImgInfo getAppStartImg() {
        List<Object> objects = this.appHomeImgDao.getProperty("imgInfo.imgInfoId", null, null);

//        IQueryParams queryParams = new GeneralQueryParams().andEqual("configType", Constant.CONFIG_APP_START_IMG);
//        List<Object> objects = this.systemConfigDao.getProperty("configValue", null, queryParams);
        if (objects == null || objects.size() < 1) {
            return null;
        }
        String imgId = objects.get(0).toString();
        return this.imgInfoService.getById(imgId);
    }

    @Override
    public int executeUpdate(Map<String, Object> paramAndValue, IQueryParams queryParams) {
        return this.systemConfigDao.executeUpdate(paramAndValue, queryParams);
    }
}
