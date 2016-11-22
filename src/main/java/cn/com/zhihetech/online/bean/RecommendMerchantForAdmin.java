package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ydc on 16-8-15.
 */
@Entity
@Table(name = "t_rec_merchant_for_admin")
public class RecommendMerchantForAdmin extends SerializableAndCloneable {

    private String recId;
    private Merchant merchant;
    private ImgInfo coverImg;
    private int recOrder;
    private String desc;

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "rec_id", length = 36, nullable = false, unique = true, updatable = false)
    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @ManyToOne
    @JoinColumn(name = "img_id", nullable = false)
    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }

    @Column(name = "rec_order", nullable = false)
    public int getRecOrder() {
        return recOrder;
    }

    public void setRecOrder(int recOrder) {
        this.recOrder = recOrder;
    }

    @Column(name = "rec_desc", length = 200)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
