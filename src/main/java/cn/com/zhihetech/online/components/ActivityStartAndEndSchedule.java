package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.vo.ActivityScheduleJob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * Created by YangDaiChun on 2016/3/16.
 */
@Component(value = "scheduleJob")
public class ActivityStartAndEndSchedule implements Job {

    private IActivityService activityService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            SchedulerContext skedCtx = context.getScheduler().getContext();
            activityService = (IActivityService) skedCtx.get("activityService");
            Object scheduleJob = context.getMergedJobDataMap().get("activityScheduleJob");
            if (scheduleJob != null && scheduleJob instanceof ActivityScheduleJob) {
                ActivityScheduleJob activityScheduleJob = (ActivityScheduleJob) scheduleJob;
                String activitId = activityScheduleJob.getActivity().getActivitId();
                if (activityScheduleJob.isStartFlag()) {
                    this.activityService.executeActivityStarted(activitId);
                } else {
                    this.activityService.executeActivityOver(activitId);
                }
            }
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setRefireImmediately(true);
            Log log = LogFactory.getLog(this.getClass());
            log.error("活动开始结束定时器出错", e);
        }
    }
}
