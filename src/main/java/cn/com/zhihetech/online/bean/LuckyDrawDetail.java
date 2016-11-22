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
@Table(name = "t_lucky_draw_detail")
public class LuckyDrawDetail extends SerializableAndCloneable {

    private String lddId;
    private LuckyDraw luckyDraw;
    private long whoNumber; //第几个人能得此奖
    private User user;  //此奖的获得者
    private Date drawDate;  //获奖者获奖日期
    private boolean useState; //是否使用true则已经被使用，false则没被使用
    private String lukyDrawCode;  //奖号
    private Date useDate;   //奖品领取时间

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "ldd_id", length = 36, nullable = false)
    public String getLddId() {
        return lddId;
    }

    public void setLddId(String lddId) {
        this.lddId = lddId;
    }

    @ManyToOne
    @JoinColumn(name = "lucky_draw_id", nullable = false)
    public LuckyDraw getLuckyDraw() {
        return luckyDraw;
    }

    public void setLuckyDraw(LuckyDraw luckyDraw) {
        this.luckyDraw = luckyDraw;
    }

    @Column(name = "who_number")
    public long getWhoNumber() {
        return whoNumber;
    }

    public void setWhoNumber(long whoNumber) {
        this.whoNumber = whoNumber;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
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

    @Column(name = "use_state")
    public boolean isUseState() {
        return useState;
    }

    public void setUseState(boolean useState) {
        this.useState = useState;
    }

    @Column(name = "lucky_draw_code", length = 36)
    public String getLukyDrawCode() {
        return lukyDrawCode;
    }

    public void setLukyDrawCode(String lukyDrawCode) {
        this.lukyDrawCode = lukyDrawCode;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "use_date")
    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }
}
