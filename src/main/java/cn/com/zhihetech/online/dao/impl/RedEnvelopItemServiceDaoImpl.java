package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.online.dao.IRedEnvelopItemServiceDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by ShenYunjie on 2015/12/14.
 */
@Repository("redEnvelopItemDao")
public class RedEnvelopItemServiceDaoImpl extends SimpleSupportDao<RedEnvelopItem> implements IRedEnvelopItemServiceDao {

    @Override
    public int executeGrab(String redEnvelopId, String userId, Date date) {
        String sql = "UPDATE t_redenvelop_item SET received=TRUE,received_date=:nowDate ,user_id =:userId WHERE envelop_id =:redEnvelopId AND received = FALSE AND user_id IS NULL AND received_date IS NULL LIMIT 1";
        Session session = this.getSession();
        Query query = session.createSQLQuery(sql);
        query.setString("redEnvelopId", redEnvelopId);
        query.setString("userId", userId);
        query.setTimestamp("nowDate", date);
        return query.executeUpdate();
    }

}
