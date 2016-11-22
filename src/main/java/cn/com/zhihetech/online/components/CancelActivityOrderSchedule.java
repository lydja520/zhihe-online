package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.service.IOrderScheduleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * Created by ydc on 16-6-7.
 */
@Component
public class CancelActivityOrderSchedule implements Job {

    private IOrderScheduleService orderScheduleService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            SchedulerContext skedCtx = context.getScheduler().getContext();
            orderScheduleService = (IOrderScheduleService) skedCtx.get("orderScheduleService");
            String orderId = (String) context.getMergedJobDataMap().get("orderId");
            this.orderScheduleService.executeAutoCancelActivityOrder(orderId);
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setRefireImmediately(true);
            Log log = LogFactory.getLog(this.getClass());
            log.error("取消订单定时任务执行失败", e);
        }
    }
}
