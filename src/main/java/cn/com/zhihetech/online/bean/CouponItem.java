package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by YangDaiChun on 2016/3/9.
 */
@Entity
@Table(name = "t_coupon_item")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CouponItem extends SerializableAndCloneable {

    private String couponItemId;
    private Coupon coupon;
    private String couponName = "打折券";
    private User user;  //领取的用户
    private Date receivedDate;  //领取时间
    private float faceValue;   //面值
    private int couponType = Constant.COUPON_DISCOUNT_TYPE; //默认为打折卷
    private boolean useState = false;  //是否已经使用,默认为未使用
    private Date useDate;  //优惠券的使用时间
    private String code;  //优惠券码
    private Date beginValidity; //优惠券可以开始使用的时间
    private Date validity;  //优惠券有效期
    private boolean validState;  //是否有效

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "coupon_item_id", length = 36, nullable = false)
    public String getCouponItemId() {
        return couponItemId;
    }

    public void setCouponItemId(String couponItemId) {
        this.couponItemId = couponItemId;
    }

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    @Column(name = "coupon_name", length = 36)
    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "received_date")
    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    @Column(name = "face_value")
    public float getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(float faceValue) {
        this.faceValue = faceValue;
    }

    @Column(name = "coupon_type", nullable = false)
    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    @Column(name = "is_use", nullable = false)
    public boolean isUseState() {
        return useState;
    }

    public void setUseState(boolean useState) {
        this.useState = useState;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "use_date", nullable = true)
    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    @Column(name = "coupon_code", nullable = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "begin_validity")
    public Date getBeginValidity() {
        return beginValidity;
    }

    public void setBeginValidity(Date beginValidity) {
        this.beginValidity = beginValidity;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "validity", nullable = false)
    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    @Transient
    public boolean isValidState() {
        if ((new Date()).before(validity)) {
            return true;
        }
        return false;
    }

    public void setValidState(boolean validState) {
        this.validState = validState;
    }
}
