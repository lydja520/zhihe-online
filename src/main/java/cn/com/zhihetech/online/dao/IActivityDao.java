package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

/**
 * Created by ShenYunjie on 2015/12/7.
 */
public interface IActivityDao extends SupportDao<Activity> {
    /**
     * 根据商品类别获取活动
     *
     * @param goodsAttSetId 商品类别ID
     * @param pager         分页参数
     * @return
     */
    PageData<Activity> getActivitiesByGoodsAttributeSetId(String goodsAttSetId, Pager pager);
}
