package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.GoodsAttribute;
import cn.com.zhihetech.online.dao.IGoodsAttributeDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ydc on 16-7-6.
 */
@Repository(value = "goodsAttributeDao")
public class GoodsAttributeDaoImpl extends SimpleSupportDao<GoodsAttribute> implements IGoodsAttributeDao{
}
