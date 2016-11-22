package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Messager;
import cn.com.zhihetech.online.dao.IMessagerDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2015/12/18.
 */
@Repository(value = "messaerDao")
public class MessagerDaoImpl extends SimpleSupportDao<Messager> implements IMessagerDao{
}
