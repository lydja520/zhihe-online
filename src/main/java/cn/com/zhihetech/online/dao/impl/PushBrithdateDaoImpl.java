package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.PushBrithdate;
import cn.com.zhihetech.online.dao.IPushBrithdateDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/3/24.
 */
@Repository(value = "pushBrithdateDao")
public class PushBrithdateDaoImpl extends SimpleSupportDao<PushBrithdate> implements IPushBrithdateDao {
}
