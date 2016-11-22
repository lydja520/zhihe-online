package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.Recommend;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/1/11.
 */
public interface IRecommendService extends SupportService<Recommend> {
    /**
     * 根据推荐ID删除推荐记录
     *
     * @param entityId
     */
    void deleteRecommendById(String entityId);

    /**
     * 根据推荐ID修改推荐排序与推荐理由
     *
     * @param recommendId
     * @param orderIndex
     * @param reason
     */
    void updateOderAndReasonById(String recommendId, int orderIndex, String reason);

    /**
     * 根据商家ID获取商家的推荐商品，如推荐商品不足指定后去总数时则返回商家最新的有效商品
     *
     * @param pager
     * @param merchantId
     * @return
     */
    List<Goods> getRecommendGoodsByMerchantId(Pager pager, String merchantId);
}
