package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.AppHomeImg;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
public interface IAppHomeImgService extends SupportService<AppHomeImg> {

    void addOrUpdateImg(AppHomeImg appHomeImg);
}
