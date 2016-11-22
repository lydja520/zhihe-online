package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 积分对应类
 * Created by YangDaiChun on 2015/11/12.
 */
@Entity
@Table(name = "t_integral")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

public class Integral extends SerializableAndCloneable {

    private String integralId;
    private User user;
    private int integralValue;
    private Date createDate;
    private String aeccWay;
    private int integralType;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "integral_id", length = 36)
    public String getIntegralId() {
        return integralId;
    }

    public void setIntegralId(String integralId) {
        this.integralId = integralId;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "integral_value")
    public int getIntegralValue() {
        return integralValue;
    }

    public void setIntegralValue(int integralValue) {
        this.integralValue = integralValue;
    }

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "aecc_way", length = 500)
    public String getAeccWay() {
        return aeccWay;
    }

    public void setAeccWay(String aeccWay) {
        this.aeccWay = aeccWay;
    }

    @Column(name = "integral_type")
    public int getIntegralType() {
        return integralType;
    }

    public void setIntegralType(int integralType) {
        this.integralType = integralType;
    }
}
