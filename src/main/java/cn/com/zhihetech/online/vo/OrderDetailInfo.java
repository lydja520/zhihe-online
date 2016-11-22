package cn.com.zhihetech.online.vo;

import java.io.Serializable;

/**
 * Created by ShenYunjie on 2016/7/14.
 */
public class OrderDetailInfo implements Serializable, Cloneable {
    private String goodsId; //商品ID
    private int count;  //购买数量
    private double price;   //商品价格
    private String skuId;   //对应的商品SKU组合ID

    public OrderDetailInfo() {
    }

    public OrderDetailInfo(String goodsId, int count, double price, String skuId) {
        this.goodsId = goodsId;
        this.count = count;
        this.price = price;
        this.skuId = skuId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}
