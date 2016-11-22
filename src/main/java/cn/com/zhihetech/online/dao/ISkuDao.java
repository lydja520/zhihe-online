package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.Sku;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

/**
 * Created by YangDaiChun on 2015/12/2.
 */
public interface ISkuDao extends SupportDao<Sku> {
    /**
     * 获取指定商品当前库存最多的商品SKU
     *
     * @param goodsId
     * @return
     */
    Sku getMaxStockSkuByGoodsId(String goodsId);

    /**
     * 根据SKU ID和购买数量更新sku的销量
     *
     * @param skuId SKU ID
     * @param aLong 购买数量
     * @return
     */
    int addSkuVolumeBySkuAndCount(String skuId, Long aLong);

    /**
     * 根据SKU ID和购买数量来减少sku的销量
     * @param skuId
     * @param count
     * @return
     */
    int subSkuVolumeBySkuAndCount(String skuId,Long count);
}
