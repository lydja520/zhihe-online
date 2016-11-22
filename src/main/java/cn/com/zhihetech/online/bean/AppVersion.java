package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ShenYunjie on 2016/3/14.
 */
@Entity
@Table(name = "t_app_version")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AppVersion extends SerializableAndCloneable {
    private String versionId;
    private int versionCode;    //版本序号
    private String versionName; //名称
    private String versionUrl;  //现在地址
    private String versionDisc; //版本描述

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "version_id", length = 36)
    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @Column(name = "version_code", nullable = false)
    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Column(name = "version_name", nullable = false, length = 50)
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Column(name = "version_url", nullable = false, length = 100)
    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    @Column(name = "version_disc", nullable = false, length = 500)
    public String getVersionDisc() {
        return versionDisc;
    }

    public void setVersionDisc(String versionDisc) {
        this.versionDisc = versionDisc;
    }
}
