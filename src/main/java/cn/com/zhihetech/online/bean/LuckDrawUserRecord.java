package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ydc on 16-4-21.
 */
@Entity
@Table(name = "t_lucky_draw_record")
public class LuckDrawUserRecord extends SerializableAndCloneable {

    private String ldrId;
    private User user;  //对应用户
    private Date drawDate;  //抽奖日期
    private long personNumber;  //是第几个人
    private LuckyDrawActivity luckyDrawActivity;

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "lucky_draw_record_id", length = 36, nullable = false)
    public String getLdrId() {
        return ldrId;
    }

    public void setLdrId(String ldrId) {
        this.ldrId = ldrId;
    }

    @ManyToOne
    @JoinColumn(name = "userId")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "draw_date")
    public Date getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }

    @Column(name = "personNumber")
    public long getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(long personNumber) {
        this.personNumber = personNumber;
    }

    @ManyToOne
    @JoinColumn(name = "lda_id")
    public LuckyDrawActivity getLuckyDrawActivity() {
        return luckyDrawActivity;
    }

    public void setLuckyDrawActivity(LuckyDrawActivity luckyDrawActivity) {
        this.luckyDrawActivity = luckyDrawActivity;
    }
}
