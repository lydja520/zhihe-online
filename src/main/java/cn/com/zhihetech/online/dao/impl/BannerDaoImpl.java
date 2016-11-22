package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.dao.IBannerDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2015/12/16.
 */
@Repository("bannerDao")
public class BannerDaoImpl extends SimpleSupportDao<Banner> implements IBannerDao{
}
