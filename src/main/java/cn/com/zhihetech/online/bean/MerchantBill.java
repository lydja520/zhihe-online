package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.util.DateUtils;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.lang.model.element.Name;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
@Entity
@Table(name = "t_merchant_bill")
public class MerchantBill extends SerializableAndCloneable {

    private String billId;
    private Merchant merchant;  //账单对应的商家
    private String aliPayAccount;   //支付宝账号
    private double amount;  //账单生成金额
    private double poundage;  //手续费
    private double realAmount;  //扣除续费后的实际金额
    private String batchCode;   //账单批次
    private Date createDate;  //账单生成日期
    private Date handleDate; //账单处理日期
    private String msg;  //账单详细信息
    private String billCode;  //账单编号
    private boolean handleState; //账单是否已经处理
    private String aliPayTransactionCode;  //支付宝交易号
    private Date startDate;  //账单的开始时间
    private Date endDate;   //账单的结束时间
    private long orderAmount; //账单中包含的订单数
    private float handlePoundageRate = Constant.MERCHANT_BILL_POUNDAGE_RATE;
    private boolean autoGenerate;  //此订单是自动定时生成，还是手动生成

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "bill_id", length = 36, nullable = false)

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Column(name = "ali_pay_account", nullable = true, length = 50)
    public String getAliPayAccount() {
        return aliPayAccount;
    }

    public void setAliPayAccount(String aliPayAccount) {
        this.aliPayAccount = aliPayAccount;
    }

    @Column(name = "amount", nullable = false)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "poundage", nullable = false)
    public double getPoundage() {
        return poundage;
    }

    public void setPoundage(double poundage) {
        this.poundage = poundage;
    }

    @Column(name = "real_amount", nullable = false)
    public double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(double realAmount) {
        this.realAmount = realAmount;
    }

    @Column(name = "batch_code", nullable = false, length = 10)
    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "handle_date")
    public Date getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }

    @Column(name = "msg")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Column(name = "bill_code", nullable = false, length = 50)
    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    @Column(name = "handle_state", nullable = false)
    public boolean isHandleState() {
        return handleState;
    }

    public void setHandleState(boolean handleState) {
        this.handleState = handleState;
    }

    @Column(name = "alipay_transaction_code", length = 50)
    public String getAliPayTransactionCode() {
        return aliPayTransactionCode;
    }

    public void setAliPayTransactionCode(String aliPayTransactionCode) {
        this.aliPayTransactionCode = aliPayTransactionCode;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date", nullable = false)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date", nullable = false)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "order_amount", nullable = false)
    public long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(long orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Transient
    public float getHandlePoundageRate() {
        return handlePoundageRate;
    }

    public void setHandlePoundageRate(float handlePoundageRate) {
        this.handlePoundageRate = handlePoundageRate;
    }

    @Column(name = "is_auto_generate", nullable = false)
    public boolean isAutoGenerate() {
        return autoGenerate;
    }

    public void setAutoGenerate(boolean autoGenerate) {
        this.autoGenerate = autoGenerate;
    }
}
