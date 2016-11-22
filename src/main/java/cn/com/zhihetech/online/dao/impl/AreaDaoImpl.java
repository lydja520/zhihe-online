package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.dao.IAreaDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by yangdaichun on 2015/11/23.
 */
@Repository("areaDao")
public class AreaDaoImpl extends SimpleSupportDao<Area> implements IAreaDao{
}
