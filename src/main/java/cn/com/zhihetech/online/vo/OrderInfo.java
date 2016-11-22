package cn.com.zhihetech.online.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/7/14.
 */
public class OrderInfo implements Serializable, Cloneable {
    private String orderName;   //订单名
    private String userId;  //用户ID
    private String userMsg; //订单留言
    private String receiverName;    //收件人姓名
    private String receiverPhone;   //收件人联系号码
    private String receiverAdd; //收件地址
    private String payType; //支付方式（只能为"alipay"或"wx"，其中alipay:为支付宝支付，wx:为微信支付）
    private float carriage;    //运费
    private double orderTotal;  //订单总金额（包括运费）
    private boolean seckillOrder = false;   //是否是秒杀订单
    private String activityGodosId; //活动商品ID(当为秒杀商品订单时此属性不能为空）
    private List<OrderDetailInfo> orderDetails;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAdd() {
        return receiverAdd;
    }

    public void setReceiverAdd(String receiverAdd) {
        this.receiverAdd = receiverAdd;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public float getCarriage() {
        return carriage;
    }

    public void setCarriage(float carriage) {
        this.carriage = carriage;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public boolean isSeckillOrder() {
        return seckillOrder;
    }

    public void setSeckillOrder(boolean seckillOrder) {
        this.seckillOrder = seckillOrder;
    }

    public String getActivityGodosId() {
        return activityGodosId;
    }

    public void setActivityGodosId(String activityGodosId) {
        this.activityGodosId = activityGodosId;
    }

    public List<OrderDetailInfo> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailInfo> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
