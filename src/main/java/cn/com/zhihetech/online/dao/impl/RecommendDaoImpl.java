package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Recommend;
import cn.com.zhihetech.online.dao.IRecommendDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/1/11.
 */
@Repository("recommendDao")
public class RecommendDaoImpl extends SimpleSupportDao<Recommend> implements IRecommendDao {
}
