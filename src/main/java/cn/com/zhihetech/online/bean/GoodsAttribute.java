package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 商品与S与商品属性组合
 * Created by ShenYunjie on 2016/7/5.
 */
@Entity
@Table(name = "t_goods_attribute", uniqueConstraints = {@UniqueConstraint(columnNames = {"goods_id", "attr_id", "attr_value", "sku_id"})})
public class GoodsAttribute extends SerializableAndCloneable {
    private String goodsAttrId;
    private Goods goods;
    private SkuAttribute attribute;
    private String attrValue;
    private Sku sku;

    public GoodsAttribute() {
    }

    public GoodsAttribute(String goodsAttrId) {
        super();
        this.goodsAttrId = goodsAttrId;
    }

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "goods_attr_id", length = 36)
    public String getGoodsAttrId() {
        return goodsAttrId;
    }

    public void setGoodsAttrId(String goodsAttrId) {
        this.goodsAttrId = goodsAttrId;
    }

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @ManyToOne
    @JoinColumn(name = "attr_id", nullable = false)
    public SkuAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(SkuAttribute attribute) {
        this.attribute = attribute;
    }

    @Column(name = "attr_value", nullable = false, length = 100)
    public String getAttrValue() {
        if (this.attrValue != null) {
            return attrValue.trim();
        }
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    @ManyToOne
    @JoinColumn(name = "sku_id", nullable = false)
    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }
}
