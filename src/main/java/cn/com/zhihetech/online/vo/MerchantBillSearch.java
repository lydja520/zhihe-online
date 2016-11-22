package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;

import java.util.Date;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
public class MerchantBillSearch {

    private String billCode;
    private String aliPayTransactionCode;
    private Merchant merchant;
    private String handleState;
    private String startTime;
    private String endTime;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getAliPayTransactionCode() {
        return aliPayTransactionCode;
    }

    public void setAliPayTransactionCode(String aliPayTransactionCode) {
        this.aliPayTransactionCode = aliPayTransactionCode;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getHandleState() {
        return handleState;
    }

    public void setHandleState(String handleState) {
        this.handleState = handleState;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void initQueryParams(IQueryParams queryParams) {
        queryParams = queryParams == null ? new GeneralQueryParams() : queryParams;
        if (merchant != null && !StringUtils.isEmpty(merchant.getMerchName())) {
            queryParams.andAllLike("merchant.merchName", merchant.getMerchName());
        }
        if (!StringUtils.isEmpty(billCode)) {
            queryParams.andAllLike("billCode", this.billCode);
        }
        if (!StringUtils.isEmpty(aliPayTransactionCode)) {
            queryParams.andAllLike("aliPayTransactionCode", aliPayTransactionCode);
        }
        if (!StringUtils.isEmpty(handleState)) {
            if (handleState.equals("1")) {
                queryParams.andEqual("handleState", true);
            }
            if (handleState.equals("2")) {
                queryParams.andEqual("handleState", false);
            }
        }
        if (!StringUtils.isEmpty(startTime)) {
            Date _start = DateUtils.getStartDateTimeWithDate(DateUtils.String2Date(startTime));
            queryParams.andMoreAndEq("createDate", _start);
        }
        if (!StringUtils.isEmpty(endTime)) {
            Date _end = DateUtils.getEndDateTimeWithDate(DateUtils.String2Date(endTime));
            queryParams.andLessAndEq("createDate", _end);
        }
        if (!queryParams.sortContainsKey("handleState")) {
            queryParams.sort("handleState", Order.ASC);
        }
        if (!queryParams.sortContainsKey("createDate")) {
            queryParams.sort("createDate", Order.ASC);
        }
    }
}
