package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 店铺图片展示
 * Created by ShenYunjie on 2016/1/11.
 */
@Entity
@Table(name = "t_merchant_display")
public class ShopShow extends SerializableAndCloneable {
    private String showId;
    private Merchant merchant;
    private ImgInfo imgInfo;
    private String showDesc; //图片描述

    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @javax.persistence.Id
    @Column(name = "show_id", length = 36)
    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
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
    public ImgInfo getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(ImgInfo imgInfo) {
        this.imgInfo = imgInfo;
    }

    @Column(name = "show_desc", length = 300)
    public String getShowDesc() {
        return showDesc;
    }

    public void setShowDesc(String showDesc) {
        this.showDesc = showDesc;
    }
}
