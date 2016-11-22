package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2016/3/12.
 */
@Entity
@Table(name = "t_user_withdraw")
public class UserWithdraw extends SerializableAndCloneable {

    private String userWithdrawId;   //id
    private User user;    //对应的用户
    private float money;  //提现的钱
    private float poundage;  //手续费
    private float realMoney;  //实际提现的钱
    private Date applyDate;  //申请提现的日期
    private Date withdrawDate;   //管理员确认提现日期
    private String operator;  //进行提现确认的企业名称
    private String aliCode;   //支付宝账号
    private String aliOrderNum;  //支付宝转账订单号
    private int withdrawState;  //状态
    private String withdrawFailureReason; //提现失败原因

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "uwd_id", length = 36, nullable = false)
    public String getUserWithdrawId() {
        return userWithdrawId;
    }

    public void setUserWithdrawId(String userWithdrawId) {
        this.userWithdrawId = userWithdrawId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "money")
    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    @Column(name = "poundage")
    public float getPoundage() {
        return poundage;
    }

    public void setPoundage(float poundage) {
        this.poundage = poundage;
    }

    @Column(name = "real_money")
    public float getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(float realMoney) {
        this.realMoney = realMoney;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "apply_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "withdraw_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getWithdrawDate() {
        return withdrawDate;
    }

    public void setWithdrawDate(Date withdrawDate) {
        this.withdrawDate = withdrawDate;
    }

    @Column(name = "opeator", length = 100, nullable = true)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "alipay_code", length = 100, nullable = false)
    public String getAliCode() {
        return aliCode;
    }

    public void setAliCode(String aliCode) {
        this.aliCode = aliCode;
    }

    @Column(name = "alipay_order_num", nullable = true)
    public String getAliOrderNum() {
        return aliOrderNum;
    }

    public void setAliOrderNum(String aliOrderNum) {
        this.aliOrderNum = aliOrderNum;
    }

    @Column(name = "withdraw_state")
    public int getWithdrawState() {
        return withdrawState;
    }

    public void setWithdrawState(int withdrawState) {
        this.withdrawState = withdrawState;
    }

    @Column(name = "withDraw_failure_reson", length = 200, nullable = true)
    public String getWithdrawFailureReason() {
        return withdrawFailureReason;
    }

    public void setWithdrawFailureReason(String withdrawFailureReason) {
        this.withdrawFailureReason = withdrawFailureReason;
    }
}
