package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2016/3/14.
 */
@Entity
@Table(name = "t_notification")
public class Notification extends SerializableAndCloneable{

    private String notificationId;  //通知Id
    private String notificationName; //通知名
    private String introduction; //简介
    private String desc;  //介绍
    private String nLink;  //跳转链接
    private boolean readState = false;  //是否已阅读

    @javax.persistence.Id
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "n_id",length = 36,nullable = false)
    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    @Column(name = "n_name")
    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    @Transient
    public String getIntroduction() {
        return desc.substring(0,10)+"...";
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Column(name = "n_desc")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Column(name = "n_link")
    public String getnLink() {
        return nLink;
    }

    public void setnLink(String nLink) {
        this.nLink = nLink;
    }

    @Column(name = "read_state")
    public boolean isReadState() {
        return readState;
    }

    public void setReadState(boolean readState) {
        this.readState = readState;
    }
}
