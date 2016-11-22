package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.dao.IUserDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
@Repository("userDao")
public class UserDaoImpl extends SimpleSupportDao<User> implements IUserDao {
}
