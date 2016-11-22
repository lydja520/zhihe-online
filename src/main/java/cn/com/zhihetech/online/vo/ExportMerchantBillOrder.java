package cn.com.zhihetech.online.vo;

/**
 * Created by ydc on 16-6-23.
 */
public class ExportMerchantBillOrder {
    private String merchentBillCode;
    private String merchantName;
    private String orderCode;
    private String orderPayDate;
    private String orderPayType;
    private String activityGoodsState;
    private String orderAmount;
    private String orderCarriage;
    private String orderPoundage;
    private String orderRealAmount;
    private String orderPoundageRate;

    public String getMerchentBillCode() {
        return merchentBillCode;
    }

    public void setMerchentBillCode(String merchentBillCode) {
        this.merchentBillCode = merchentBillCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderPayDate() {
        return orderPayDate;
    }

    public void setOrderPayDate(String orderPayDate) {
        this.orderPayDate = orderPayDate;
    }

    public String getOrderPayType() {
        if ("wx".equals(this.orderPayType)) {
            return "微信";
        } else if ("alipay".equals(this.orderPayType)) {
            return "支付宝";
        }
        return orderPayType;
    }

    public void setOrderPayType(String orderPayType) {
        this.orderPayType = orderPayType;
    }

    public String getActivityGoodsState() {
        return activityGoodsState;
    }

    public void setActivityGoodsState(String activityGoodsState) {
        this.activityGoodsState = activityGoodsState;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderCarriage() {
        return orderCarriage;
    }

    public void setOrderCarriage(String orderCarriage) {
        this.orderCarriage = orderCarriage;
    }

    public String getOrderPoundage() {
        return orderPoundage;
    }

    public void setOrderPoundage(String orderPoundage) {
        this.orderPoundage = orderPoundage;
    }

    public String getOrderRealAmount() {
        return orderRealAmount;
    }

    public void setOrderRealAmount(String orderRealAmount) {
        this.orderRealAmount = orderRealAmount;
    }

    public String getOrderPoundageRate() {
        return orderPoundageRate;
    }

    public void setOrderPoundageRate(String orderPoundageRate) {
        this.orderPoundageRate = orderPoundageRate;
    }
}
