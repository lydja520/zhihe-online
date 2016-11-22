package cn.com.zhihetech.online.bean;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2015/11/12.
 */
@Entity
@Table(name = "tmp_order_detail")
public class TmpOrderDetail extends SerializableAndCloneable {

    private String orderDetailId;
    private TmpOrder tmpOrder;
    private Goods goods;
    private long goodsCount;//购买数量
    private float price;  //商品单价
    private String skuId;  //skuId
    private String skuName;   //sku组合名称

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "detial_id", length = 36)
    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    public TmpOrder getTmpOrder() {
        return tmpOrder;
    }

    public void setTmpOrder(Order TmpOrder) {
        this.tmpOrder = tmpOrder;
    }

    @Column(name = "goods_id", nullable = false)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @JSONField(name = "count")
    @Column(name = "goods_count", nullable = false)
    public long getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(long goodsCount) {
        this.goodsCount = goodsCount;
    }

    @Column(name = "price", nullable = false)
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Column(name = "sku_id", length = 36, nullable = false)
    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    @Column(name = "sku_name", nullable = true)
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
