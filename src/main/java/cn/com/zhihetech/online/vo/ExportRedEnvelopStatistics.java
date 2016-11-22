package cn.com.zhihetech.online.vo;

import java.util.Date;

/**
 * Created by Administrator on 2016/6/27.
 */
public class ExportRedEnvelopStatistics {
    private String merchName;
    private String activitName;
    private Date createDate;
    private String payState; //红包的钱是否已经通过支付包打给实淘
    private String sended; //是否已发出
    private float totalMoney;
    private int numbers;
    private float receivedTotalMoney;//红包已领取金额
    private int receivedTotal;//红包已领取个数
    private float surplusMoney;//红包剩余金额
    private int surplusTotal;//红包剩余个数

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getActivitName() {
        return activitName;
    }

    public void setActivitName(String activitName) {
        this.activitName = activitName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getSended() {
        return sended;
    }

    public void setSended(String sended) {
        this.sended = sended;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public float getReceivedTotalMoney() {
        return receivedTotalMoney;
    }

    public void setReceivedTotalMoney(float receivedTotalMoney) {
        this.receivedTotalMoney = receivedTotalMoney;
    }

    public int getReceivedTotal() {
        return receivedTotal;
    }

    public void setReceivedTotal(int receivedTotal) {
        this.receivedTotal = receivedTotal;
    }

    public float getSurplusMoney() {
        return surplusMoney;
    }

    public void setSurplusMoney(float surplusMoney) {
        this.surplusMoney = surplusMoney;
    }

    public int getSurplusTotal() {
        return surplusTotal;
    }

    public void setSurplusTotal(int surplusTotal) {
        this.surplusTotal = surplusTotal;
    }
}
