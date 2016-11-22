package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by YangDaiChun on 2015/11/12.
 */
@Entity
@Table(name = "t_order")
public class Order extends SerializableAndCloneable {

    private String orderId;
    private String orderName;
    private User user;
    private Merchant merchant;
    private String orderCode;
    private float orderTotal;  //商家修改后的订单价格，如果为修改，则为订单原来的价格
    private float originalOrderTotal;  //订单原来的价格
    private int orderState = Constant.ORDER_STATE_NO_SUBMIT;
    private Date createDate;   //订单创建日期
    private Date payDate;    //订单支付日期
    private Date dispatcherDate;   //订单发货日期
    private Date deliverDate;   //订单收货日期
    private Date evaluateDate;  //订单评价日期
    private Date applyRefundDate;  //订单申请退款日期
    private Date confirmRefundDate;  //商家确认订单确认退款日期
    private Date refundOkDate;   //订单退款成功日期
    private Date cancelOrderDate;  //订单取消日期
    private String payType;
    private String userMsg;
    private String receiverName;
    private String receiverPhone;
    private String receiverAdd;
    private String orderDetailInfo;
    private String pingPPorderNo;
    private float carriage = 0; //运费
    private String carriageNum;  //快递单号
    private float refund = 0;  //退款金额
    private String pingPPId;
    private boolean deleteState = false;
    private ActivityGoods activityGoods;
    private ImgInfo coverImg;  //订单封面图

    private String alipayRefundUrl; //支付宝退款地址（支付宝支付申请退款之后支付宝返回的退款地址）
    private String alipayRefundTransacCode;
    private String stateDisplay;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "order_id", length = 36)

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name = "order_name", nullable = false, length = 500)
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "order_code", nullable = false, length = 50)
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @Column(name = "order_total", nullable = false)
    public float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    @Column(name = "original_order_total")
    public float getOriginalOrderTotal() {
        return originalOrderTotal;
    }

    public void setOriginalOrderTotal(float originalOrderTotal) {
        this.originalOrderTotal = originalOrderTotal;
    }

    @Column(name = "order_state", nullable = false)
    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dispatcher_date")
    public Date getDispatcherDate() {
        return dispatcherDate;
    }

    public void setDispatcherDate(Date dispatcherDate) {
        this.dispatcherDate = dispatcherDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deliver_date")
    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "evaluate_date")
    public Date getEvaluateDate() {
        return evaluateDate;
    }

    public void setEvaluateDate(Date evaluateDate) {
        this.evaluateDate = evaluateDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "pay_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "apply_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getApplyRefundDate() {
        return applyRefundDate;
    }

    public void setApplyRefundDate(Date applyRefundDate) {
        this.applyRefundDate = applyRefundDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "confirm_refund_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getConfirmRefundDate() {
        return confirmRefundDate;
    }

    public void setConfirmRefundDate(Date confirmRefundDate) {
        this.confirmRefundDate = confirmRefundDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "refund_ok_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRefundOkDate() {
        return refundOkDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "cancel_order_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCancelOrderDate() {
        return cancelOrderDate;
    }

    public void setCancelOrderDate(Date cancelOrderDate) {
        this.cancelOrderDate = cancelOrderDate;
    }

    public void setRefundOkDate(Date refundOkDate) {
        this.refundOkDate = refundOkDate;
    }

    @Column(name = "pay_type")
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Column(name = "user_msg", nullable = true, length = 500)
    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }

    @Column(name = "receiver_name", nullable = false, length = 50)
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    @Column(name = "receiver_phone", nullable = false, length = 20)
    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    @Column(name = "receiver_add", nullable = false, length = 400)
    public String getReceiverAdd() {
        return receiverAdd;
    }

    public void setReceiverAdd(String receiverAdd) {
        this.receiverAdd = receiverAdd;
    }

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Transient
    public String getOrderDetailInfo() {
        return orderDetailInfo;
    }

    public void setOrderDetailInfo(String orderDetailInfo) {
        this.orderDetailInfo = orderDetailInfo;
    }

    @Column(name = "pingPP_order_no")
    public String getPingPPorderNo() {
        return pingPPorderNo;
    }

    public void setPingPPorderNo(String pingPPorderNo) {
        this.pingPPorderNo = pingPPorderNo;
    }

    @Column(name = "carriage")
    public float getCarriage() {
        return carriage;
    }

    public void setCarriage(float carriage) {
        this.carriage = carriage;
    }

    @Column(name = "carriage_num", nullable = true, length = 100)
    public String getCarriageNum() {
        return carriageNum;
    }

    public void setCarriageNum(String carriageNum) {
        this.carriageNum = carriageNum;
    }

    @Column(name = "refund", nullable = true)
    public float getRefund() {
        return refund;
    }

    public void setRefund(float refund) {
        this.refund = refund;
    }

    @Column(name = "pingPP_id", length = 36)
    public String getPingPPId() {
        return pingPPId;
    }

    public void setPingPPId(String pingPPId) {
        this.pingPPId = pingPPId;
    }

    @Column(name = "delete_state")
    public boolean isDeleteState() {
        return deleteState;
    }

    public void setDeleteState(boolean deleteState) {
        this.deleteState = deleteState;
    }

    @ManyToOne
    @JoinColumn(name = "ag_id", nullable = true)
    public ActivityGoods getActivityGoods() {
        return activityGoods;
    }

    public void setActivityGoods(ActivityGoods activityGoods) {
        this.activityGoods = activityGoods;
    }

    @ManyToOne
    @JoinColumn(name = "img_info_id")
    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }

    @Column(name = "refund_url", nullable = true, length = 400)
    public String getAlipayRefundUrl() {
        return alipayRefundUrl;
    }

    public void setAlipayRefundUrl(String alipayRefundUrl) {
        this.alipayRefundUrl = alipayRefundUrl;
    }

    @Transient
    public String getStateDisplay() {
        switch (this.orderState) {
            case Constant.ORDER_STATE_ALREADY_CANCEL:
                return "订单已取消";
        }
        return stateDisplay;
    }

    public void setStateDisplay(String stateDisplay) {
        this.stateDisplay = stateDisplay;
    }

    @Column(name = "alipay_refund_trans_code", nullable = true)
    public String getAlipayRefundTransacCode() {
        return alipayRefundTransacCode;
    }

    public void setAlipayRefundTransacCode(String alipayRefundTransacCode) {
        this.alipayRefundTransacCode = alipayRefundTransacCode;
    }
}
