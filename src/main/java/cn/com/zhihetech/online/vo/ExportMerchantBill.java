package cn.com.zhihetech.online.vo;

/**
 * Created by ydc on 16-6-23.
 */
public class ExportMerchantBill {
    private String billCode;
    private String merchantName;
    private String createDate;
    private String startAndEndDate;
    private String amount;
    private String poundage;
    private String realAmount;
    private String poundatgeRate;
    private String merchantAliCode;
    private String handleState;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStartAndEndDate() {
        return startAndEndDate;
    }

    public void setStartAndEndDate(String startAndEndDate) {
        this.startAndEndDate = startAndEndDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPoundage() {
        return poundage;
    }

    public void setPoundage(String poundage) {
        this.poundage = poundage;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getPoundatgeRate() {
        return poundatgeRate;
    }

    public void setPoundatgeRate(String poundatgeRate) {
        this.poundatgeRate = poundatgeRate;
    }

    public String getMerchantAliCode() {
        return merchantAliCode;
    }

    public void setMerchantAliCode(String merchantAliCode) {
        this.merchantAliCode = merchantAliCode;
    }

    public String getHandleState() {
        return handleState;
    }

    public void setHandleState(String handleState) {
        this.handleState = handleState;
    }
}
