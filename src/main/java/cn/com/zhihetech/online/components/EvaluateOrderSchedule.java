package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.service.IOrderScheduleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;

/**
 * Created by YangDaiChun on 2016/7/22.
 */
public class EvaluateOrderSchedule implements Job {

    private IOrderScheduleService orderScheduleService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            SchedulerContext skedCtx = jobExecutionContext.getScheduler().getContext();
            orderScheduleService = (IOrderScheduleService) skedCtx.get("orderScheduleService");
            String orderId = (String) jobExecutionContext.getMergedJobDataMap().get("orderId");
            this.orderScheduleService.executeEvaluateOrder(orderId);
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setRefireImmediately(true);
            Log log = LogFactory.getLog(this.getClass());
            log.error("取消订单定时任务执行失败", e);
        }
    }
}
