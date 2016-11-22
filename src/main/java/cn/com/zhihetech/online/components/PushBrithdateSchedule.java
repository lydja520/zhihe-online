package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IPushBrithdateService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by YangDaiChun on 2016/3/24.
 */
@Component
public class PushBrithdateSchedule extends QuartzJobBean implements StatefulJob {

    private IPushBrithdateService pushBrithdateService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SchedulerContext skedCtx = null;
        try {
            skedCtx = jobExecutionContext.getScheduler().getContext();
            pushBrithdateService = (IPushBrithdateService) skedCtx.get("pushBrithdateService");
            this.pushBrithdateService.addPushBrithdates();
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setRefireImmediately(true);
            Log log = LogFactory.getLog(PushBrithdateSchedule.class);
            log.error("生日推送定时任务创建失败!",e);
        }
    }
}
