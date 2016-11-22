package cn.com.zhihetech.online.service;

/**
 * Created by ShenYunjie on 2016/7/14.
 */
public interface IUtilService {
    /**
     * 初始化商品价格库存，及商品默认sku属性
     */
    void initInfo();

    /**
     * 初始化商品默认sku
     */
    void initSku();

    /**
     * 将商品的价格、销量、最小价格、最新价格等信息跟新为对应的sku信息
     */
    void initGoodsStockAndVolumeInfo();

    /**
     * 初始化购物车数据对应的sku
     */
    void initShoppingCartInfo();
}
