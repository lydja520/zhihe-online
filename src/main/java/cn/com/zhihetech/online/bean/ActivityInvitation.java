package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 活动邀请
 * Created by ShenYunjie on 2015/12/7.
 */
@Entity
@Table(name = "t_activity_invitation")
public class ActivityInvitation extends SerializableAndCloneable {

    public final static int INVITATION_INVITATION_STATE = 1;    //邀请中
    public final static int INVITATION_AGREED_STATE = 2;    //同意邀请
    public final static int INVITATION_REFUSED_STATE = 3;   //拒绝邀请

    private String invitationId;
    private Activity activity;
    private Merchant merchant;  //受邀商家
    private Date createDate;    //邀请时间
    private Date expiredDate;   //到期时间
    private String invitedMsg;  //邀请留言
    private MerchantAlliance alliance;  //对应的商家联盟
    private int invitationState = INVITATION_INVITATION_STATE;  //默认为邀请中，受邀商家还未处理
    private String refuseReason;  //拒绝原因

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "invitation_id")
    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    @ManyToOne
    @JoinColumn(name = "acitivity_id", nullable = false)
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @ManyToOne
    @JoinColumn(name = "invited_merch", nullable = false)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_date", nullable = false)
    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    @Column(name = "invited_msg", length = 300)
    public String getInvitedMsg() {
        return invitedMsg;
    }

    public void setInvitedMsg(String invitedMsg) {
        this.invitedMsg = invitedMsg;
    }

    @Column(name = "invitation_state", nullable = false)
    public int getInvitationState() {
        return invitationState;
    }

    public void setInvitationState(int invitationState) {
        this.invitationState = invitationState;
    }

    @Column(name = "refuse_reason", length = 500)
    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    @OneToOne
    @JoinColumn(name = "alliance_id", nullable = false)
    public MerchantAlliance getAlliance() {
        return alliance;
    }

    public void setAlliance(MerchantAlliance alliance) {
        this.alliance = alliance;
    }
}
