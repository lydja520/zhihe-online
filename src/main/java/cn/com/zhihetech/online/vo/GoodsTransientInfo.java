package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;
import cn.com.zhihetech.online.util.NumberUtils;

/**
 * Created by ShenYunjie on 2016/7/12.
 */
public class GoodsTransientInfo extends SerializableAndCloneable {
    private String goodsId; //对应的商品ID
    private long stock;      //商品的原始库存量
    private long currentStock;  //商品的现有库存量
    private long volume;  //商品的销量
    private double price;          //商品的价格（默认对应最小价格）
    private double minPrice;    //最小价格
    private double maxPrice;    //最大价格

    public GoodsTransientInfo() {
    }

    public GoodsTransientInfo(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getCurrentStock() {
        return currentStock < 0 ? 0 : this.currentStock;
    }

    public void setCurrentStock(long currentStock) {
        this.currentStock = currentStock;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getPrice() {
        return NumberUtils.doubleScale(2, this.price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMinPrice() {
        return NumberUtils.doubleScale(2, this.minPrice);
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return NumberUtils.doubleScale(2, this.maxPrice);
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
