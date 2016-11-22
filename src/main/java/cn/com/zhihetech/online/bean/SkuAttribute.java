package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2015/12/2.
 * <p>
 * sku属性名称与商品类别关联
 */
@Entity
@Table(name = "t_sku_attribute", uniqueConstraints = {@UniqueConstraint(columnNames = {"goods_attr_set_id", "sku_att_name"})})
public class SkuAttribute extends SerializableAndCloneable {

    private String skuAttId;
    private String skuAttCode; //sku属性编号
    private String skuAttName; //sku属性名:如颜色，尺码等
    private String skuAttDesc;
    private GoodsAttributeSet goodsAttributeSet;
    private boolean permit = true; //是否有效
    private boolean required = true;   //是否是必须的

    public SkuAttribute() {
    }

    public SkuAttribute(String attrId) {
        super();
        this.skuAttId = attrId;
    }


    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "sku_att_id", length = 36)
    public String getSkuAttId() {
        return skuAttId;
    }

    public void setSkuAttId(String skuAttId) {
        this.skuAttId = skuAttId;
    }

    @Column(name = "sku_att_code", length = 36)
    public String getSkuAttCode() {
        return skuAttCode;
    }

    public void setSkuAttCode(String skuAttCode) {
        this.skuAttCode = skuAttCode;
    }

    @Column(name = "sku_att_name", length = 300)
    public String getSkuAttName() {
        return skuAttName;
    }

    public void setSkuAttName(String skuAttName) {
        this.skuAttName = skuAttName;
    }

    @Column(name = "sku_att_desc")
    public String getSkuAttDesc() {
        return skuAttDesc;
    }

    public void setSkuAttDesc(String skuAttDesc) {
        this.skuAttDesc = skuAttDesc;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goods_attr_set_id", nullable = false)
    public GoodsAttributeSet getGoodsAttributeSet() {
        return goodsAttributeSet;
    }

    public void setGoodsAttributeSet(GoodsAttributeSet goodsAttributeSet) {
        this.goodsAttributeSet = goodsAttributeSet;
    }

    @Column(name = "permit", nullable = false)
    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    @Column(name = "required", nullable = false)
    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
