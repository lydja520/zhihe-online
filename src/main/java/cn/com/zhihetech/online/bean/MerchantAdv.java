package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 商家广告，商家推广
 * Created by ShenYunjie on 2016/4/22.
 */
@Entity
@Table(name = "t_merchant_adv")
public class MerchantAdv extends SerializableAndCloneable {

    /**
     * 推广位置（此广告所在位置）
     */
    public enum AdvLocation {
        realsemeet, //新闻发布会页面
        anywhere    //任何地方
    }

    public String advId;
    public ImgInfo advImg;  // 广告封面图
    public Merchant merchant;
    public Date beginDateTime;  //开始时间
    public Date endDateTime;    //结束时间
    public AdvLocation location = AdvLocation.realsemeet;   //所在位置（默认在新闻发布会）
    public int advOrder = 0;    //排序
    public Date createDateTime;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "adv_id", length = 36, nullable = false)
    public String getAdvId() {
        return advId;
    }

    public void setAdvId(String advId) {
        this.advId = advId;
    }

    @ManyToOne
    @JoinColumn(name = "adv_img", nullable = false)
    public ImgInfo getAdvImg() {
        return advImg;
    }

    public void setAdvImg(ImgInfo advImg) {
        this.advImg = advImg;
    }

    @ManyToOne
    @JoinColumn(name = "adv_merchant", nullable = false)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "begin_date_time")
    public Date getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(Date beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date_time")
    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "adv_location", nullable = false)
    public AdvLocation getLocation() {
        return location;
    }

    public void setLocation(AdvLocation location) {
        this.location = location;
    }

    @Column(name = "adv_order", nullable = false)
    public int getAdvOrder() {
        return advOrder;
    }

    public void setAdvOrder(int advOrder) {
        this.advOrder = advOrder;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date_time", nullable = false, updatable = false)
    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }
}
