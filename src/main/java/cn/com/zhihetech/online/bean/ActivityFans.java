package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
@Entity
@Table(name = "activity_fans")
public class ActivityFans extends SerializableAndCloneable {
    private String fansId;
    private Activity activity;  //对应的活动
    private Merchant invitationMerch;   //邀请加入的商家
    private Date joinDate;  //加入活动时间
    private User fans;  //用户

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "fans_id", length = 36)
    public String getFansId() {
        return fansId;
    }

    public void setFansId(String fansId) {
        this.fansId = fansId;
    }

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity", nullable = false)
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @ManyToOne
    @JoinColumn(name = "invitation_merchant", nullable = true)
    public Merchant getInvitationMerch() {
        return invitationMerch;
    }

    public void setInvitationMerch(Merchant invitationMerch) {
        this.invitationMerch = invitationMerch;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "join_date", nullable = false, updatable = false)
    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    public User getFans() {
        return fans;
    }

    public void setFans(User fans) {
        this.fans = fans;
    }
}
