package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

/**
 * Created by YangDaiChun on 2016/3/4.
 */
public interface IActivityGoodsDao extends SupportDao<ActivityGoods>{

    int executeSubCount(String agId, int agCount);

    void executeAddCount(String agId, int count);
}
