package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.SystemConfig;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.Map;

/**
 * Created by ShenYunjie on 2016/3/18.
 */
public interface ISystemConfigService extends SupportService<SystemConfig> {

    /**
     * 获取App启动页图片
     *
     * @return
     */
    ImgInfo getAppStartImg();

    int executeUpdate(Map<String, Object> paramAndValue, IQueryParams queryParams);
}
