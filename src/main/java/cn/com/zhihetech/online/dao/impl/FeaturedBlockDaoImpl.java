package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.FeaturedBlock;
import cn.com.zhihetech.online.dao.IFeaturedBlockDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/3/10.
 */
@Repository(value = "featuredBlockDao")
public class FeaturedBlockDaoImpl extends SimpleSupportDao<FeaturedBlock> implements IFeaturedBlockDao {
}
