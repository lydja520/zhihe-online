package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.GoodsBanner;
import cn.com.zhihetech.online.dao.IGoodsBannerDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YangDaiChun on 2016/1/7.
 */
@Repository(value = "goodsBannerDao")
public class GoodsBannerDaoImpl extends SimpleSupportDao<GoodsBanner> implements IGoodsBannerDao{
}
