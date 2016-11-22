package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/13.
 */

@Entity
@Table(name = "t_login_journal")
public class LoginJournal extends SerializableAndCloneable {

    public enum LoginType {
        user, merchant
    }

    private String loginJournalId;
    private String userCode;
    private Date loginDate;
    private String mobileName;      //手机型号
    private String loginIP;
    private boolean success;
    private float longitude;               //登录位置的经度
    private float latitude;                  //登录位置的纬度
    private String osName;              //手机操作系统类型
    private String osVersion;           //手机操作系统版本
    private String failReason;  //登录失败原因（只用当登录失败是才有效）
    private float appVersionCode; //当前app版本号
    private String appVersionName;
    private LoginType loginType = LoginType.user;

    public LoginJournal() {
    }

    public LoginJournal(RequestHeader header) {
        this.loginIP = header.getRemoteAddr();
        this.osName = header.getOsName();
        this.osVersion = header.getOsVersion();
        this.appVersionCode = header.getAppVersionCode();
        this.appVersionName = header.getAppVersionName();
        this.mobileName = header.getMobileName();
    }


    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "journal_id", length = 36)
    public String getLoginJournalId() {
        return loginJournalId;
    }

    public void setLoginJournalId(String loginJournalId) {
        this.loginJournalId = loginJournalId;
    }

    @Column(name = "user_code")
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "login_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    @Column(name = "mobile_name", length = 50)
    public String getMobileName() {
        return mobileName;
    }

    public void setMobileName(String mobileName) {
        this.mobileName = mobileName;
    }

    @Column(name = "login_ip", length = 20)
    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    @Column(name = "longitude")
    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Column(name = "latitude")
    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @Column(name = "os_name", length = 50)
    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    @Column(name = "os_version", length = 50)
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Column(name = "login_success", nullable = false)
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Column(name = "fail_reason", length = 500)
    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    @Column(name = "app_version_code")
    public float getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(float appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    @Column(name = "app_version_name")
    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }
}
