package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.SystemConfig;
import cn.com.zhihetech.online.dao.ISystemConfigDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/3/18.
 */
@Repository("systemConfigDao")
public class SystemConfigDaoImpl extends SimpleSupportDao<SystemConfig> implements ISystemConfigDao {

}
