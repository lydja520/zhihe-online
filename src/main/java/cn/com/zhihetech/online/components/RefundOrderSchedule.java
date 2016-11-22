package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.service.IOrderDetailService;
import cn.com.zhihetech.online.service.IOrderScheduleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * 买家申请退款后，卖家没有做处理，则在指定时间后进行自动退款操作
 * Created by ydc on 16-6-21.
 */
@Component(value = "refundOrderSchedule")
public class RefundOrderSchedule implements Job {

    private IOrderScheduleService orderScheduleService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            SchedulerContext skedCtx = jobExecutionContext.getScheduler().getContext();
            orderScheduleService = (IOrderScheduleService) skedCtx.get("orderScheduleService");
            String orderId = (String) jobExecutionContext.getMergedJobDataMap().get("orderId");
            this.orderScheduleService.executeRefund(orderId);
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setRefireImmediately(true);
            Log log = LogFactory.getLog(this.getClass());
            log.error("定时退款失败！", e);
        }
    }
}
