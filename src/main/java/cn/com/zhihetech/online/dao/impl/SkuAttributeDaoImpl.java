package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.SkuAttribute;
import cn.com.zhihetech.online.dao.ISkuAttributeDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2015/12/2.
 */
@Repository("skuAttributeDao")
public class SkuAttributeDaoImpl extends SimpleSupportDao<SkuAttribute> implements ISkuAttributeDao{

}
