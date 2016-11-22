package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.dao.IGoodsAttributeSetDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2015/12/4.
 */
@Repository("goodsAttributeSetDao")
public class GoodsAttributeSetDaoImpl extends SimpleSupportDao<GoodsAttributeSet> implements IGoodsAttributeSetDao{
}
