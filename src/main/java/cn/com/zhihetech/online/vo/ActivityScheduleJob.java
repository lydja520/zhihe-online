package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.bean.SerializableAndCloneable;

import java.util.Date;

/**
 * Created by YangDaiChun on 2016/3/1.
 */
public class ActivityScheduleJob extends SerializableAndCloneable{

    /** 任务名称 **/
    private String jobName;

    /** 任务分组 **/
    private String jobGroup;

    /** 任务运行时间表达式 **/
    private Date cronExpression;

    /**需要执行的活动任务*/
    private Activity activity;

    /**表示活动任务的状态，true表示开始，false表示结束*/
    private boolean startFlag;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public Date getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(Date cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isStartFlag() {
        return startFlag;
    }

    public void setStartFlag(boolean startFlag) {
        this.startFlag = startFlag;
    }
}
