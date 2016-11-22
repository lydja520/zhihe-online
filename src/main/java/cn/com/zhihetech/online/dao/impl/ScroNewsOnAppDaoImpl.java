package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ScroNewsOnApp;
import cn.com.zhihetech.online.dao.IScroNewsOnAppDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/8/19.
 */
@Repository(value = "scroNewOnAppDao")
public class ScroNewsOnAppDaoImpl extends SimpleSupportDao<ScroNewsOnApp> implements IScroNewsOnAppDao{
}
