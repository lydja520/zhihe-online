package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.District;
import cn.com.zhihetech.online.dao.IDistrictDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2015/11/20.
 */
@Repository("districtDao")
public class DistrictDaoImpl extends SimpleSupportDao<District> implements IDistrictDao {
}
