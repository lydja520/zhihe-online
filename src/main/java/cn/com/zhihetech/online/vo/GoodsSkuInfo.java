package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;
import cn.com.zhihetech.online.bean.Sku;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/7/8.
 */
public class GoodsSkuInfo extends SerializableAndCloneable {
    private String skuId;   //skuId
    private String goodsId; //此sku对应的商品ID
    private String mixCode; //属性组合（MD5加密），对应Sku中的mixCode
    private double price;    //单价
    private int currentStock;  //当前库存
    private int volume; //销量
    private String coverImgUrl; //封面图片
    private List<String> attrCodes;  //商品属性ID与对应属性值用“：”分隔组合成的字符串列表（如：["xxx:M","xxxx:红色","xxxxx:短袖"]，其中：xxx为属性ID,M为此属性对应的值)
    private String skuValue;

    public GoodsSkuInfo() {
    }

    public GoodsSkuInfo(Sku sku) {
        this.skuId = sku.getSkuId();
        this.goodsId = sku.getGoodsId();
        this.mixCode = sku.getMixCode();
        this.price = sku.getPrice();
        this.currentStock = sku.getCurrentStock();
        this.volume = (int) sku.getVolume();
        this.coverImgUrl = sku.getCoverImg() == null ? null : sku.getCoverImg().getUrl();
        this.skuValue = sku.getSkuValue();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getMixCode() {
        return mixCode;
    }

    public void setMixCode(String mixCode) {
        this.mixCode = mixCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public List<String> getAttrCodes() {
        return attrCodes;
    }

    public void setAttrCodes(List<String> attrCodes) {
        this.attrCodes = attrCodes;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }
}
