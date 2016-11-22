package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ydc on 16-5-27.
 */
@Entity
@Table(name = "t_activity_order")
public class ActivityOrder extends SerializableAndCloneable {

    private String activityOrderId;
    private Activity activity;   //该订单对应的活动
    private String orderCode;  //订单号
    private String orderName;  //订单名
    private String extMsg;  //订单扩展信息
    private double totalMoney;  //订单金额
    private Merchant merchant;  //订单对应的参与活动的商家
    private Date createDate;  //订单创建时间
    private boolean payState;  //是否已经支付
    private Date payDate;  //支付日期
    private String aliTransactionCode;  //阿里支付交易号
    private String sellerAccount;  //收款方支付宝账号
    private String buyerAccount; //支付方支付账号

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "activity_order_id", length = 36, nullable = false)
    public String getActivityOrderId() {
        return activityOrderId;
    }

    public void setActivityOrderId(String activityOrderId) {
        this.activityOrderId = activityOrderId;
    }

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Column(name = "order_code", nullable = false, length = 50)
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
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

    @Column(name = "total_money", nullable = false)
    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Column(name = "order_name", nullable = false)
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @Column(name = "ext_msg", nullable = true)
    public String getExtMsg() {
        return extMsg;
    }

    public void setExtMsg(String extMsg) {
        this.extMsg = extMsg;
    }

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Column(name = "pay_state", nullable = false)
    public boolean isPayState() {
        return payState;
    }

    public void setPayState(boolean payState) {
        this.payState = payState;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payDate", nullable = true)
    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    @Column(name = "ali_transaction_code", nullable = true)
    public String getAliTransactionCode() {
        return aliTransactionCode;
    }

    public void setAliTransactionCode(String aliTransactionCode) {
        this.aliTransactionCode = aliTransactionCode;
    }

    @Column(name = "seller_account", nullable = true)
    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    @Column(name = "buyer_account", nullable = true)
    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }
}
