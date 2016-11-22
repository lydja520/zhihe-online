package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.dao.IRedEnvelopDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2015/12/11.
 */
@Repository("redEnvelopDao")
public class RedEnvelopDaoImpl extends SimpleSupportDao<RedEnvelop> implements IRedEnvelopDao {
}
