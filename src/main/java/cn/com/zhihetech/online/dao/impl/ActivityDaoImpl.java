package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IActivityDao;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.SubQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.apache.commons.logging.Log;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/12/7.
 */
@Repository("activityDao")
public class ActivityDaoImpl extends SimpleSupportDao<Activity> implements IActivityDao {
    /**
     * 根据商品类别获取活动
     *
     * @param goodsAttSetId 商品类别ID
     * @param pager         分页参数
     * @return
     */
    @Override
    public PageData<Activity> getActivitiesByGoodsAttributeSetId(String goodsAttSetId, Pager pager) {
        StringBuilder hql = new StringBuilder("from Activity ac join ac.attributeSets at ")
                .append("where at.goodsAttSetId = :goodsAttSetId and ")
                .append("(ac.currentState = :activityStartedState or ac.currentState = :activityExaminedOkState)");

        Map<String, Object> params = new HashMap<>();
        params.put("goodsAttSetId", goodsAttSetId);
        params.put("activityStartedState", Constant.ACTIVITY_STATE_STARTED);
        params.put("activityExaminedOkState", Constant.ACTIVITY_STATE_EXAMINED_OK);

        Long total = getRecordTotal("select count(distinct ac) " + hql.toString(), params);

        hql.append(" order by ac.currentState desc,ac.beginDate");    //按开始转台和开始最近时间排序
        Query query = createQueryWithHQL("select distinct ac " + hql.toString());
        initQueryValues(query, params);
        initQueryPage(query, pager);
        List<Activity> activities = query.list();

        PageData<Activity> pageData = new PageData<Activity>(total, pager)
                .setRows(activities);
        return pageData;
    }
}
