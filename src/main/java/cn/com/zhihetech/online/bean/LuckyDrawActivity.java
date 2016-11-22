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
@Table(name = "t_lucky_draw_activity")
public class LuckyDrawActivity extends SerializableAndCloneable{

    private String activityId;
    private Date createDate;  //活动创建时间
    private String desc;  //活动描述
    private String activityName; //活动名称
    private boolean permit; //活动状态，true上线，false下线
    private boolean submitState; //提交状态

    @javax.persistence.Id
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "activity_id",length = 36,nullable = false)
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "content_desc")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Column(name = "activity_name",length = 100)
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Column(name = "permit")
    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    @Column(name = "submit_state")
    public boolean isSubmitState() {
        return submitState;
    }

    public void setSubmitState(boolean submitState) {
        this.submitState = submitState;
    }
}
