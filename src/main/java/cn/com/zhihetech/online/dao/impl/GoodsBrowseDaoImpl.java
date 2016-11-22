package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.GoodsBrowse;
import cn.com.zhihetech.online.dao.IGoodsBrowseDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/4/18.
 */
@Repository("goodsBrowseDao")
public class GoodsBrowseDaoImpl extends SimpleSupportDao<GoodsBrowse> implements IGoodsBrowseDao {
}
