package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.AppHomeImg;
import cn.com.zhihetech.online.dao.IAppHomeImgDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
@Repository(value = "appHomeImgDao")
public class AppHomeImgDaoImpl extends SimpleSupportDao<AppHomeImg> implements IAppHomeImgDao {

}
