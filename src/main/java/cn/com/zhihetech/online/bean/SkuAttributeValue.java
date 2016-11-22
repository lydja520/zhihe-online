package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ShenYunjie on 2016/7/5.
 */
/*@Entity
@Table(name = "t_sku_attribute_value", uniqueConstraints = {@UniqueConstraint(columnNames = {"attr_value", "attr_id"})})*/
public class SkuAttributeValue extends SerializableAndCloneable {
    private String valueId;
    private String attrValue;
    private SkuAttribute attribute;
    private String creator; //创建者（如果是平台自行维护则为null,否则为商家ID)
    private String valueDesc;   //描述
    private boolean permit;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "attr_value_id", length = 36)
    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    @Column(name = "attr_value", nullable = false, length = 200)
    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attr_id", nullable = false)
    public SkuAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(SkuAttribute attribute) {
        this.attribute = attribute;
    }

    @Column(name = "creator", nullable = true, length = 40)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name = "value_desc", nullable = true, length = 500)
    public String getValueDesc() {
        return valueDesc;
    }

    public void setValueDesc(String valueDesc) {
        this.valueDesc = valueDesc;
    }

    @Column(name = "permit", nullable = false)
    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }
}
