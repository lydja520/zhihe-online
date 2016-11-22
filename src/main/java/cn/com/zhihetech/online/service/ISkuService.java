package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.GoodsAttribute;
import cn.com.zhihetech.online.bean.Sku;
import cn.com.zhihetech.online.vo.GoodsAttrSkuInfo;
import cn.com.zhihetech.online.vo.GoodsSkuInfo;
import cn.com.zhihetech.online.vo.SkuListInfo;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2015/12/2.
 */
public interface ISkuService extends SupportService<Sku> {

    /**
     * 根据商品ID，商品属性组合，价格、库存及sku封面图添加商品sku
     *
     * @param goodsId
     * @param goodsAttrs
     * @param sku
     */
    void addSkuByGoodsId(String goodsId, List<GoodsAttribute> goodsAttrs, Sku sku);

    /**
     * 根据商品ID获取商品对应的属性、属性值和sku
     *
     * @param goodsId 商品ID
     * @return
     */
    GoodsAttrSkuInfo getGoodsAttrSkuInfoByGoodsId(String goodsId);

    /**
     * 根据商品ID获取GoodsSkuInfo
     *
     * @param goodsId
     * @return
     */
    List<GoodsSkuInfo> getGoodsSkuInfosByGoodsId(String goodsId);

    /**
     * 根据商品ID获取SkuListInfo
     *
     * @param goodsId
     * @return
     */
    List<SkuListInfo> getSkuListInfoByGoodsId(String goodsId);

    /**
     * 商品属性组合和sku编辑
     *
     * @param goodsAttrs   商品，商品属性组合
     * @param sku          需要编辑的sku
     * @param currentStock Sku当前库存
     */
    void updateGoodsAttrAndSku(List<GoodsAttribute> goodsAttrs, Sku sku, long currentStock);

    /**
     * 根据商品ID获取此商品的SKU列表
     *
     * @param goodsId
     * @return
     */
    List<Sku> getSkuListByGoodsId(String goodsId);

    /**
     * 获取指定商品默认的SKU
     *
     * @param goodsId
     * @return
     */
    Sku getDefaultSkuByGoodsId(String goodsId);

    /**
     * 根据SKU ID和购买数量更新sku的销量
     *
     * @param skuAndCount
     */
    void executeAddSkuVolumeBySkuAndCount(Map<String, Long> skuAndCount);

    /**
     * 根据sku的Id获取实时的商品sku组合情况
     * @param skuId
     * @return
     */
    String getSkuValueBySkuId(String skuId);
}
