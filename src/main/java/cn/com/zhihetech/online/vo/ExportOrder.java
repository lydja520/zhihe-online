package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.commons.Constant;

/**
 * Created by ydc on 16-6-20.
 */
public class ExportOrder {

    private String orderCreateDate;
    private String orderCode;  //订单号
    private String orderState;  //订单状态
    private String merchantName;
    private String merchantTell;
    private String merchantMobileNo;
    private String userphone;
    private String receiverName;
    private String receiverPhone;
    private String receiverAdd;
    private String goodsName;  //订单中某给商品的商品名
    private String goodsSkuValue; //订单中某商品的属性值
    private String goodsCount;  //订单中某个商品的购买数量

    public String getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(String orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderState() {
        return this.orderState;
    }

    public void setOrderState(String orderState) {
        String _orderState = null;
        int orderStateNum = Integer.parseInt(orderState);
        switch (orderStateNum) {
            case Constant.ORDER_STATE_NO_SUBMIT:
                _orderState = "订单未提交";
                break;
            case Constant.ORDER_STATE_NO_PAYMENT:
                _orderState = "已提交，待支付";
                break;
            case Constant.ORDER_STATE_NO_DISPATCHER:
                _orderState = "已支付，待发货";
                break;
            case Constant.ORDER_STATE_ALREADY_DISPATCHER:
                _orderState = "已发货,待收货";
                break;
            case Constant.ORDER_STATE_ALREADY_DELIVER:
                _orderState = "已确认收货";
                break;
            case Constant.ORDER_STATE_ALREADY_CANCEL:
                _orderState = "订单已取消";
                break;
            case Constant.ORDER_STATE_WAIT_REFUND:
                _orderState = "订单等待退款";
                break;
            case Constant.ORDER_STATE_ALREADY_REFUND:
                _orderState = "订单退款成功";
                break;
            case Constant.ORDER_STATE_ALREADY_EVALUATE:
                _orderState = "买家已经评论";
                break;
            case Constant.ORDER_STATE_REFUNDING:
                _orderState = "退款中";
                break;
            case Constant.ORDER_STATE_ALREADY_GENERATE_BILL:
                _orderState = "已经添加到账单，等待官方结算";
                break;
            case Constant.ORDER_STATE_ALREADY_BILL:
                _orderState = "官方已经对该订单进行结算";
                break;
            case Constant.ORDER_STATE_PAYDING:
                _orderState = "支付中";
        }
        this.orderState = _orderState;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantTell() {
        return merchantTell;
    }

    public void setMerchantTell(String merchantTell) {
        this.merchantTell = merchantTell;
    }

    public String getMerchantMobileNo() {
        return merchantMobileNo;
    }

    public void setMerchantMobileNo(String merchantMobileNo) {
        this.merchantMobileNo = merchantMobileNo;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getGoodsSkuValue() {
        return goodsSkuValue;
    }

    public void setGoodsSkuValue(String goodsSkuValue) {
        this.goodsSkuValue = goodsSkuValue;
    }
}
