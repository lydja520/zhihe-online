package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

@Entity
@Table(name = "t_big_brand")
public class BigBrand extends SerializableAndCloneable {

    private String bigBrandId;
    private Merchant merchant;
    private int brandOrder;
    private Date createDate;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "banner_id", length = 36)
    public String getBigBrandId() {
        return bigBrandId;
    }

    public void setBigBrandId(String bigBrandId) {
        this.bigBrandId = bigBrandId;
    }

    @ManyToOne
    @JoinColumn(name = "merch_id")
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Column(name = "brand_order")
    public int getBrandOrder() {
        return brandOrder;
    }

    public void setBrandOrder(int brandOrder) {
        this.brandOrder = brandOrder;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
