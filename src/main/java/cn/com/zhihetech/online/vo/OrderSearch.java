package cn.com.zhihetech.online.vo;

/**
 * Created by YangDaiChun on 2016/3/29.
 */
public class OrderSearch {

    private String searchOrderCode;
    private String searchOrderState;
    private String searchCarriageNum;
    private String searchUserPhone;
    private String searchMerchantName;
    private String searchStartTime;
    private String searchEndTime;

    public String getSearchOrderCode() {
        return searchOrderCode;
    }

    public void setSearchOrderCode(String searchOrderCode) {
        this.searchOrderCode = searchOrderCode;
    }

    public String getSearchOrderState() {
        return searchOrderState;
    }

    public void setSearchOrderState(String searchOrderState) {
        this.searchOrderState = searchOrderState;
    }

    public String getSearchCarriageNum() {
        return searchCarriageNum;
    }

    public void setSearchCarriageNum(String searchCarriageNum) {
        this.searchCarriageNum = searchCarriageNum;
    }

    public String getSearchUserPhone() {
        return searchUserPhone;
    }

    public void setSearchUserPhone(String searchUserPhone) {
        this.searchUserPhone = searchUserPhone;
    }

    public String getSearchMerchantName() {
        return searchMerchantName;
    }

    public void setSearchMerchantName(String searchMerchantName) {
        this.searchMerchantName = searchMerchantName;
    }

    public String getSearchStartTime() {
        return searchStartTime;
    }

    public void setSearchStartTime(String searchStartTime) {
        this.searchStartTime = searchStartTime;
    }

    public String getSearchEndTime() {
        return searchEndTime;
    }

    public void setSearchEndTime(String searchEndTime) {
        this.searchEndTime = searchEndTime;
    }
}
