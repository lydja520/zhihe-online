package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING, length = 20)
//@DiscriminatorValue("common_user")
@Entity
@Table(name = "t_user")
public class User extends SerializableAndCloneable {
    private String userId;
    private String userPhone;
    private String userName;
    private String pwd;
    private int age;
    private Area area;
    private String occupation;
    private String income;
    private boolean sex;
    private Date birthday;
    private boolean permit;
    private String invitCode;
    private Date createDate;
    private ImgInfo portrait;   //用户头像
    private String chatUserName;  //对应的环信用户账号
    private String chatPassword;    //对应的环信密码

    public User() {
    }

    public User(String userId) {
        this.userId = userId;
    }

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "user_id", length = 36)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @ManyToOne
    @JoinColumn(name = "area_id")
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Column(name = "occupation", length = 100)
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @Column(name = "income", length = 100)
    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    @Column(name = "user_name", length = 50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "user_phone", length = 20, unique = true, nullable = false)
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Column(name = "user_sex")
    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    @JSONField(format = Constant.DEFAULT_DATE_FORMAT)
    @Column(name = "user_birthday")
    @Temporal(TemporalType.DATE)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "user_pwd", length = 40, nullable = false)
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Column(name = "permit")
    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "invit_code", length = 20)
    public String getInvitCode() {
        return invitCode;
    }

    public void setInvitCode(String invitCode) {
        this.invitCode = invitCode;
    }

    @Transient
    public int getAge() {
        if (birthday != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int _now = calendar.get(Calendar.YEAR);
            calendar.setTime(birthday);
            int _birthday = calendar.get(Calendar.YEAR);
            return _now - _birthday;
        }
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @ManyToOne
    @JoinColumn(name = "portrait", nullable = true)
    public ImgInfo getPortrait() {
        return portrait;
    }

    public void setPortrait(ImgInfo portrait) {
        this.portrait = portrait;
    }

    @Column(name = "chat_user_name", nullable = false, length = 36)
    public String getChatUserName() {
        return chatUserName;
    }

    public void setChatUserName(String chatUserName) {
        this.chatUserName = chatUserName;
    }

    @Column(name = "chat_password", nullable = false, length = 40)
    public String getChatPassword() {
        return chatPassword;
    }

    public void setChatPassword(String chatPassword) {
        this.chatPassword = chatPassword;
    }
}

