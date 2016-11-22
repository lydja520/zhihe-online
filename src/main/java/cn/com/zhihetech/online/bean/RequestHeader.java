package cn.com.zhihetech.online.bean;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2016/4/19.
 */
public class RequestHeader {

    private String mobileName;
    private String osName;
    private String osVersion;
    private String token;
    private String userCode;
    private float appVersionCode;
    private String appVersionName;
    private String remoteAddr;  //IP地址

    public RequestHeader() {
    }

    public RequestHeader(HttpServletRequest request) {
        mobileName = request.getHeader("mobileName");
        osName = request.getHeader("osName");
        osVersion = request.getHeader("osVersion");
        token = request.getHeader("token");
        userCode = request.getHeader("userCode");
        try {
            appVersionCode = Float.parseFloat(request.getHeader("appVersionCode"));
        } catch (Exception e) {
        }
        appVersionName = request.getHeader("appVersionName");
        this.remoteAddr = request.getRemoteAddr();
    }

    public String getMobileName() {
        return mobileName;
    }

    public void setMobileName(String mobileName) {
        this.mobileName = mobileName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public float getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(float appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }
}
