package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.AppVersion;
import cn.com.zhihetech.online.dao.IAppVersionDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/3/14.
 */
@Repository("appVersionDao")
public class AppVersionDaoImpl extends SimpleSupportDao<AppVersion> implements IAppVersionDao {
}
