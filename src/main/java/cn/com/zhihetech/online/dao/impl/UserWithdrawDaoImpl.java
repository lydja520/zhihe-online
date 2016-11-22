package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.UserWithdraw;
import cn.com.zhihetech.online.dao.IUserWithdrawDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/3/12.
 */
@Repository(value = "userWithdrawDao")
public class UserWithdrawDaoImpl extends SimpleSupportDao<UserWithdraw> implements IUserWithdrawDao {
}
