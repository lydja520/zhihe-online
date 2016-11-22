package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 商家联盟
 * Created by ShenYunjie on 2015/12/7.
 */
@Entity
@Table(name = "t_merchant_alliance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchantAlliance extends SerializableAndCloneable {

    public final static int ALLIANCE_UNEXECUTED_STATE = ActivityInvitation.INVITATION_INVITATION_STATE; //未处理
    public final static int ALLIANCE_AGREED_STATE = ActivityInvitation.INVITATION_AGREED_STATE; //已同意
    public final static int ALLIANCE_REFUSED_STATE = ActivityInvitation.INVITATION_REFUSED_STATE;   //已拒绝

    public final static int ALLIANCE_READINESS_STATE = 4;   //已准备就绪，同意参加活动的商家必须提交将状态更新为此状态，否则活动发起上将无法提交活动申请

    private String allianceId;
    private String allianceName;    //默认为对应的活动名称
    private Activity activity;  //此联盟对应的活动
    private Merchant merchant;   //对应的商家
    private Date createDate;    //对应商家加入此联盟的时间
    private int allianceState = ALLIANCE_UNEXECUTED_STATE;
    private String allianceDesc;
    private boolean promoted;   //是否是活动发起商家
    private float activityBudget; //该商家已经上缴的活动费用

    public MerchantAlliance() {
    }

    public MerchantAlliance(ActivityInvitation invitation) {
        this.activity = invitation.getActivity();
        this.merchant = invitation.getMerchant();
        this.createDate = invitation.getCreateDate();
    }

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "alliance_id", length = 36)
    public String getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(String allianceId) {
        this.allianceId = allianceId;
    }

    @Column(name = "alliance_name", length = 100)
    public String getAllianceName() {
        return allianceName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
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

    @Column(name = "alliance_state", nullable = false)
    public int getAllianceState() {
        return allianceState;
    }

    public void setAllianceState(int allianceState) {
        this.allianceState = allianceState;
    }

    @Column(name = "alliance_desc", length = 200)
    public String getAllianceDesc() {
        return allianceDesc;
    }

    public void setAllianceDesc(String allianceDesc) {
        this.allianceDesc = allianceDesc;
    }

    @Column(name = "promoted", updatable = false)
    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    @Column(name = "activity_budget", nullable = true)
    public float getActivityBudget() {
        return activityBudget;
    }

    public void setActivityBudget(float activityBudget) {
        this.activityBudget = activityBudget;
    }
}
