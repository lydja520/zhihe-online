package cn.com.zhihetech.online.vo;

/**
 * Created by ydc on 16-6-24.
 */
public class ExportMercahntBillOrderDetail {

    private String orderCode; //订单号
    private String userPhone;  //购买者的账号
    private String userName;  //购买者名字
    private String goodsName; //购买的商品名
    private String goodsCount;  //购买该商品的数量
    private String goodPrice; //商品的单价
    private String acrivityGoodsState; //是否购买的是活动商品
    private String carriageNum; //快递单号

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(String goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getAcrivityGoodsState() {
        return acrivityGoodsState;
    }

    public void setAcrivityGoodsState(String acrivityGoodsState) {
        this.acrivityGoodsState = acrivityGoodsState;
    }

    public String getCarriageNum() {
        return carriageNum;
    }

    public void setCarriageNum(String carriageNum) {
        this.carriageNum = carriageNum;
    }
}
