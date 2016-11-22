package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2016/3/24.
 */
@Entity
@Table(name = "t_push_brithday")
public class PushBrithdate extends SerializableAndCloneable {

    private String pushBrithdateId;
    private FocusMerchant focusMerchant;
    private Date nowBrithDay;
    private boolean pushState;

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "push_brithdate_id")
    public String getPushBrithdateId() {
        return pushBrithdateId;
    }

    public void setPushBrithdateId(String pushBrithdateId) {
        this.pushBrithdateId = pushBrithdateId;
    }

    @ManyToOne
    @JoinColumn(name = "focus_merchant_id")
    public FocusMerchant getFocusMerchant() {
        return focusMerchant;
    }

    public void setFocusMerchant(FocusMerchant focusMerchant) {
        this.focusMerchant = focusMerchant;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "now")
    public Date getNowBrithDay() {
        return nowBrithDay;
    }

    public void setNowBrithDay(Date nowBrithDay) {
        this.nowBrithDay = nowBrithDay;
    }

    @Column(name = "push_sate")
    public boolean isPushState() {
        return pushState;
    }

    public void setPushState(boolean pushState) {
        this.pushState = pushState;
    }
}
