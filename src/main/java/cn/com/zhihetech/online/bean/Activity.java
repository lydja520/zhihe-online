package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by ShenYunjie on 2015/12/4.
 */
@Entity
@Table(name = "t_common_activitie")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "activity_flag", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("common_activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Activity extends SerializableAndCloneable {
    private String activitId;
    private String activitName;
    private String activitDetail;   //活动详情介绍
    private ImgInfo coverImg;   //活动封面图
    private Merchant activityPromoter;   //活动发起商家
    private ActivityCategory category;  //活动类别
    private GoodsAttributeSet attributeSet; //活动商品的类别
    private int currentState = Constant.ACTIVITY_STATE_UNSBUMIT; //活动当前状态（对应常数中的活动状态）
    private String examinMsg;
    private Date beginDate; //活动开始时间
    private Date endDate;   //活动结束时间
    private Date createDate;    //活动创建时间
    private String contacterName;   //活动负责人姓名
    private String contactTell;   //活动负责人联系电话
    private String activitDesc; //活动备注信息
    private String displayState; //显示当前状态
    private String chatRoomId;
    private float activityBudget; //活动经费，等于该活动所有商家联盟的缴费之和

    private Set<GoodsAttributeSet> attributeSets;   //活动对应的商品类别

    private ReceptionRoom receptionRoom;    //对应会客厅

    public Activity() {
    }

    public Activity(String activitId) {
        this.activitId = activitId;
    }

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "acitivit_id", length = 36)
    public String getActivitId() {
        return activitId;
    }

    public void setActivitId(String activitId) {
        this.activitId = activitId;
    }

    @Column(name = "activit_name", nullable = false, length = 200)
    public String getActivitName() {
        return activitName;
    }

    public void setActivitName(String activitName) {
        this.activitName = activitName;
    }

    @Column(name = "detail", nullable = false, length = 500)
    public String getActivitDetail() {
        return activitDetail;
    }

    public void setActivitDetail(String activitDetail) {
        this.activitDetail = activitDetail;
    }

    @ManyToOne
    @JoinColumn(name = "cover_img", nullable = true)
    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }

    @ManyToOne
    @JoinColumn(name = "promoter", nullable = false, updatable = false)
    public Merchant getActivityPromoter() {
        return activityPromoter;
    }

    public void setActivityPromoter(Merchant activityPromoter) {
        this.activityPromoter = activityPromoter;
    }

    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    public ActivityCategory getCategory() {
        return category;
    }

    public void setCategory(ActivityCategory category) {
        this.category = category;
    }

    @Column(name = "examin_msg", nullable = true)
    public String getExaminMsg() {
        return examinMsg;
    }

    public void setExaminMsg(String examinMsg) {
        this.examinMsg = examinMsg;
    }

    @ManyToOne
    @JoinColumn(name = "attrute_set", nullable = true)
    public GoodsAttributeSet getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(GoodsAttributeSet attributeSet) {
        this.attributeSet = attributeSet;
    }

    @Column(name = "state")
    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "begin_date", nullable = false)
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date", nullable = false)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "contacter", nullable = false, length = 50)
    public String getContacterName() {
        return contacterName;
    }

    public void setContacterName(String contacterName) {
        this.contacterName = contacterName;
    }

    @Column(name = "contact_tell", nullable = false, length = 20)
    public String getContactTell() {
        return contactTell;
    }

    public void setContactTell(String contactTell) {
        this.contactTell = contactTell;
    }

    @Column(name = "activit_desc", length = 500)
    public String getActivitDesc() {
        return activitDesc;
    }

    public void setActivitDesc(String activitDesc) {
        this.activitDesc = activitDesc;
    }

    @Transient
    public String getDisplayState() {
        switch (currentState) {
            case Constant.ACTIVITY_STATE_UNSBUMIT:
                displayState = "未提交申请";
                break;
            case Constant.ACTIVITY_STATE_SBUMITED:
                displayState = "待审核";
                break;
            case Constant.ACTIVITY_STATE_EXAMINED_NOT:
                displayState = "审核未通过";
                break;
            case Constant.ACTIVITY_STATE_EXAMINED_OK:
                displayState = "已审核";
                break;
            case Constant.ACTIVITY_STATE_STARTED:
                displayState = "已开始";
                break;
            case Constant.ACTIVITY_STATE_FNISHED:
                displayState = "已结束";
                break;
        }
        return displayState;
    }

    public void setDisplayState(String displayState) {
        this.displayState = displayState;
    }

    @Column(name = "chat_room_id", nullable = true)
    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    @ManyToOne
    @JoinColumn(name = "reception_room", nullable = false)
    public ReceptionRoom getReceptionRoom() {
        return receptionRoom;
    }

    public void setReceptionRoom(ReceptionRoom receptionRoom) {
        this.receptionRoom = receptionRoom;
    }

    @Column(name = "activity_budget", nullable = true)
    public float getActivityBudget() {
        return activityBudget;
    }

    public void setActivityBudget(float activityBudget) {
        this.activityBudget = activityBudget;
    }

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = GoodsAttributeSet.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "t_activity_attribut", joinColumns = {@JoinColumn(name = "activity_id")}, inverseJoinColumns = {@JoinColumn(name = "attribut_id")})
    public Set<GoodsAttributeSet> getAttributeSets() {
        return attributeSets;
    }

    public void setAttributeSets(Set<GoodsAttributeSet> attributeSets) {
        this.attributeSets = attributeSets;
    }
}
