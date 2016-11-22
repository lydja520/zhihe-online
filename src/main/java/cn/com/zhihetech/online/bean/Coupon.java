package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 商家优惠券（包括优惠券和代金券）
 * Created by ShenYunjie on 2015/12/15.
 */
@Entity
@Table(name = "t_coupon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Coupon extends SerializableAndCloneable {
    private String couponId;
    private String couponName = "打折券";
    private float faceValue;   //面值
    private String couponMsg;   //使用规则
    private int total;  //共有多少份
    private int totalReceived;  //被领取数量
    private int surplus;    //剩余数量
    private Merchant merchant;  //优惠券所对应的商家
    private Activity activity;  //优惠券对应的活动
    private Date createDate;
    private Date startValidity; //有效起始时间
    private Date endValidity;   //有效期结束时间
    private int couponType = Constant.COUPON_DISCOUNT_TYPE; //默认为打折卷
    private boolean deleted = false;    //是否已删除

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "coupon_id", length = 36)
    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    @Column(name = "coupon_name", length = 50, nullable = false)
    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    @Column(name = "face_value", nullable = false)
    public float getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(float faceValue) {
        this.faceValue = faceValue;
    }

    @Column(name = "coupon_msg", nullable = false, length = 300)
    public String getCouponMsg() {
        return couponMsg;
    }

    public void setCouponMsg(String couponMsg) {
        this.couponMsg = couponMsg;
    }

    @Column(name = "total", nullable = false)
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Column(name = "total_received", nullable = false)
    public int getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(int totalReceived) {
        this.totalReceived = totalReceived;
    }

    @ManyToOne
    @JoinColumn(name = "merchant", nullable = false)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @ManyToOne
    @JoinColumn(name = "activity")
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @JSONField(format = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_validity")
    public Date getStartValidity() {
        return startValidity;
    }

    public void setStartValidity(Date startValidity) {
        this.startValidity = startValidity;
    }

    @JSONField(format = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_validity")
    public Date getEndValidity() {
        return endValidity;
    }

    public void setEndValidity(Date endValidity) {
        this.endValidity = endValidity;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Transient
    public int getSurplus() {
        return this.total - this.totalReceived;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }

    @Column(name = "coupon_type", nullable = false)
    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    @Column(name = "deleted", nullable = false)
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
