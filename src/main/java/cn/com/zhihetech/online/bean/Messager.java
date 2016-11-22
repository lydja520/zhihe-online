package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2015/12/18.
 */
@Entity
@Table(name = "t_messager")
public class Messager extends SerializableAndCloneable {

    private String messagerId;
    private String phoneNumber;
    private String securityCode;  //发送的验证码
    private String securityMsg;    //发送的验证信息
    private Date sendDate;          //发送时间
    private Date validity;             //有效期
    private int scState;   //验证码类型 1位注册用户，2为忘记密码修改密码，3为提现

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "id", length = 36)
    public String getMessagerId() {
        return messagerId;
    }

    public void setMessagerId(String messagerId) {
        this.messagerId = messagerId;
    }

    @Column(name = "phone_number", nullable = false, length = 36)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "security_code", length = 36, nullable = true)
    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    @Column(name = "security_msg", length = 500)
    public String getSecurityMsg() {
        return securityMsg;
    }

    public void setSecurityMsg(String securityMsg) {
        this.securityMsg = securityMsg;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "send_date", updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "validity")
    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    @Column(name = "security_state", nullable = false)
    public int getScState() {
        return scState;
    }

    public void setScState(int scState) {
        this.scState = scState;
    }
}
