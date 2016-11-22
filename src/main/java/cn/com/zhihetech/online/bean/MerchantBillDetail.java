package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2016/5/9.
 */
@Entity
@Table(name = "t_merchant_bill_detail")
public class MerchantBillDetail {

    private String billDetailId;
    private MerchantBill merchantBill; //对应的商家账单
    private Order order;
    private double amount;  //订单金额
    private double poundage;  //手续费
    private double realAmount;  //扣除手续费后的实际金额
    private float handlePoundageRate = Constant.ORDER_HANDLER_POUNDAGE_RATE;
    private Date createDate;  //生成时间
    private boolean handleState; //是否已经生成了账单

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "bill_detail_id", length = 36, nullable = false)
    public String getBillDetailId() {
        return billDetailId;
    }

    public void setBillDetailId(String billDetailId) {
        this.billDetailId = billDetailId;
    }

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = true)
    public MerchantBill getMerchantBill() {
        return merchantBill;
    }

    public void setMerchantBill(MerchantBill merchantBill) {
        this.merchantBill = merchantBill;
    }

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Column(name = "bill_detail_amount", nullable = false)
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

    @Transient
    public float getHandlePoundageRate() {
        return handlePoundageRate;
    }

    public void setHandlePoundageRate(float handlePoundageRate) {
        this.handlePoundageRate = handlePoundageRate;
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

    @Column(name = "handle_state",nullable = false)
    public boolean isHandleState() {
        return handleState;
    }

    public void setHandleState(boolean handleState) {
        this.handleState = handleState;
    }
}
