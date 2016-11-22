package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.AppVersion;
import cn.com.zhihetech.util.hibernate.service.SupportService;
import org.jvnet.hk2.annotations.Service;

/**
 * Created by Administrator on 2016/3/14.
 */
public interface IAppVersionService extends SupportService<AppVersion> {

    /**
     * 获取最新版本
     *
     * @return
     */
    AppVersion getLastVersion();
}
