package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.GoodsAttribute;
import cn.com.zhihetech.online.bean.ImgInfo;

import java.util.List;

/**
 * Created by YangDaiChun on 2016/7/8.
 */
public class SkuListInfo {

    private String skuId;
    private List<GoodsAttribute> goodsAttributes;
    private ImgInfo coverImg;
    private double price;
    private long volume;
    private long stock; //原始库存
    private long currentStock; //现有库存
    private String skuValue;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public List<GoodsAttribute> getGoodsAttributes() {
        return goodsAttributes;
    }

    public void setGoodsAttributes(List<GoodsAttribute> goodsAttributes) {
        this.goodsAttributes = goodsAttributes;
    }

    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public long getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(long currentStock) {
        this.currentStock = currentStock;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }
}
