package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.online.dao.IActivityGoodsDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/3/4.
 */
@Repository(value = "activityGoodsDao")
public class ActivityGoodsImpl extends SimpleSupportDao<ActivityGoods> implements IActivityGoodsDao {

    @Override
    public int executeSubCount(String agId, int agCount) {
        String sql = "UPDATE t_activity_goods SET ag_count=ag_count-:agCount1 WHERE ag_id =:agId AND ag_count >= :agCount2";
        Session session = this.getSession();
        Query query = session.createSQLQuery(sql);
        query.setString("agId", agId);
        query.setInteger("agCount1", agCount);
        query.setInteger("agCount2", agCount);
        int count = query.executeUpdate();
        return count;
    }

    @Override
    public void executeAddCount(String agId, int count) {
        String sql = "UPDATE t_activity_goods SET ag_count=ag_count+:agCount WHERE ag_id =:agId";
        Session session = this.getSession();
        Query query = session.createSQLQuery(sql);
        query.setString("agId", agId);
        query.setInteger("agCount", count);
        query.executeUpdate();
    }

}
