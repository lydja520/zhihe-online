package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Navigation;
import cn.com.zhihetech.online.dao.INavigationDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2015/12/21.
 */
@Repository(value = "navigationDao")
public class NavigationDaoImpl extends SimpleSupportDao<Navigation> implements INavigationDao {
}
