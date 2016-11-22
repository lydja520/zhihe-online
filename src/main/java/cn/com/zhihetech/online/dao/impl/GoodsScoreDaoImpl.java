package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.GoodsScore;
import cn.com.zhihetech.online.dao.IGoodsScoreDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/2/18.
 */
@Repository(value = "goodsScoreDao")
public class GoodsScoreDaoImpl extends SimpleSupportDao<GoodsScore> implements IGoodsScoreDao {
}
