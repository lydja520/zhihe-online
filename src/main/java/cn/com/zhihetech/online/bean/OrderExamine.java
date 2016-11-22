package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ShenYunjie on 2016/6/29.
 */
@Entity
@Table(name = "t_order_examine")
public class OrderExamine extends SerializableAndCloneable {
    private String examineId;
    private String payType;
    private String batchCode;   //对应的账单批次
    private String orderId; //平台订单ID （如果是批量支付则可能有多个，用","分隔）
    private String orderCode;   //平台订单号（如果是批量支付则可能有多个，用","分隔）
    private String payOrderCode;    //支付订单号（对应微信（支付宝）商户订单号）
    private double orderAmount;  //平台订单金额
    private double payAmount;    //支付金额（对应微信（支付宝）订单金额）
    private boolean batchOrder; //是否是批量订单
    private boolean examineOk;  //支付金额和平台订单金额是否相等
    private Date examineDate;   //审核日期
    private String examineMsg;  //审核备注

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "examine_id", length = 36)
    public String getExamineId() {
        return examineId;
    }

    public void setExamineId(String examineId) {
        this.examineId = examineId;
    }

    @Column(name = "pay_type", nullable = false, length = 20)
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Column(name = "batch_code", nullable = false, length = 10)
    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    @Column(name = "order_id", length = 500)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name = "order_code", length = 500)
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @Column(name = "pay_order_code", length = 40)
    public String getPayOrderCode() {
        return payOrderCode;
    }

    public void setPayOrderCode(String payOrderCode) {
        this.payOrderCode = payOrderCode;
    }

    @Column(name = "order_amount")
    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Column(name = "pay_amount")
    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    @Column(name = "batch_order", nullable = false)
    public boolean isBatchOrder() {
        return batchOrder;
    }

    public void setBatchOrder(boolean batchOrder) {
        this.batchOrder = batchOrder;
    }

    @Column(name = "examine_ok", nullable = false)
    public boolean isExamineOk() {
        return examineOk;
    }

    public void setExamineOk(boolean examineOk) {
        this.examineOk = examineOk;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "examine_date", nullable = false)
    public Date getExamineDate() {
        return examineDate;
    }

    public void setExamineDate(Date examineDate) {
        this.examineDate = examineDate;
    }

    @Column(name = "examine_msg", length = 500)
    public String getExamineMsg() {
        return examineMsg;
    }

    public void setExamineMsg(String examineMsg) {
        this.examineMsg = examineMsg;
    }
}
