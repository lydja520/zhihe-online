package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

@Entity
@Table(name = "t_goods_banner")
public class GoodsBanner extends SerializableAndCloneable{

    private String goodsBannerId;
    private ImgInfo imgInfo;
    private Goods goods;
    private int bannerOrder;
    private Date createDate;


    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "banner_id", length = 36)
    public String getGoodsBannerId() {
        return goodsBannerId;
    }

    public void setGoodsBannerId(String goodsBannerId) {
        this.goodsBannerId = goodsBannerId;
    }

    @ManyToOne
    @JoinColumn(name = "img_id")
    public ImgInfo getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(ImgInfo imgInfo) {
        this.imgInfo = imgInfo;
    }

    @ManyToOne
    @JoinColumn(name = "goods_id")
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Column(name = "banner_order")
    public int getBannerOrder() {
        return bannerOrder;
    }

    public void setBannerOrder(int bannerOrder) {
        this.bannerOrder = bannerOrder;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
