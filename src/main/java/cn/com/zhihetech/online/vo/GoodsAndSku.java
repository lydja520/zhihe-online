package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.SerializableAndCloneable;
import cn.com.zhihetech.online.bean.Sku;
import cn.com.zhihetech.online.commons.Constant;

import java.util.Date;
import java.util.List;

/**
 * Created by ydc on 16-7-6.
 */
public class GoodsAndSku extends SerializableAndCloneable {

    private String goodsId;
    private String goodsName; //商品名
    private boolean onsale; //是否已上架（只有经过审核的商品才可以上架销售）
    private int examinState;//是否审核通过（商品一经下架如需重新上架则需重新审核,默认为未提交审核）
    private String examinMsg;   //审核结果
    private String goodsDesc;  //商品描述
    private long stock;      //商品的原始库存量
    private long currentStock;  //商品的现有库存量
    private long volume;  //商品的销量
    private double price;          //商品的价格
    private boolean isPick;  //是否可自取货
    private String carriageMethod; //商品邮寄方式
    private float carriage; //运费
    private Date createDate;  //创建时间
    private String ImgUrl;
    private List<GoodsAndSku> children;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public boolean isOnsale() {
        return onsale;
    }

    public void setOnsale(boolean onsale) {
        this.onsale = onsale;
    }

    public int getExaminState() {
        return examinState;
    }

    public void setExaminState(int examinState) {
        this.examinState = examinState;
    }

    public String getExaminMsg() {
        return examinMsg;
    }

    public void setExaminMsg(String examinMsg) {
        this.examinMsg = examinMsg;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<GoodsAndSku> getChildren() {
        return children;
    }

    public void setChildren(List<GoodsAndSku> children) {
        this.children = children;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getCurrentStock() {
        return currentStock;
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
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isPick() {
        return isPick;
    }

    public void setPick(boolean pick) {
        isPick = pick;
    }

    public String getCarriageMethod() {
        return carriageMethod;
    }

    public void setCarriageMethod(String carriageMethod) {
        this.carriageMethod = carriageMethod;
    }

    public float getCarriage() {
        return carriage;
    }

    public void setCarriage(float carriage) {
        this.carriage = carriage;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
