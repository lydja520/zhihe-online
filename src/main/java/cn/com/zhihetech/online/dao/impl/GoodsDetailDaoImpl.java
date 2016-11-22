package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.GoodsDetail;
import cn.com.zhihetech.online.dao.IGoodsDetailDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/1/7.
 */
@Repository(value = "goodsDetailDao")
public class GoodsDetailDaoImpl extends SimpleSupportDao<GoodsDetail> implements IGoodsDetailDao{
}
