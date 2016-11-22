package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.service.IOrderDetailService;
import cn.com.zhihetech.online.service.IOrderScheduleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * Created by ydc on 16-6-21.
 * 卖家发货后，几天内买家未确认收货则自动收货并好评
 */
@Component(value = "payOrderSchedule")
public class ConfirmReceiveOrderSchedule implements Job {

    private IOrderScheduleService orderScheduleService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            SchedulerContext skedCtx = jobExecutionContext.getScheduler().getContext();
            orderScheduleService = (IOrderScheduleService) skedCtx.get("orderScheduleService");
            String orderId = (String) jobExecutionContext.getMergedJobDataMap().get("orderId");
            this.orderScheduleService.executeconfirmReceiverOrder(orderId);
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setRefireImmediately(true);
            Log log = LogFactory.getLog(this.getClass());
            log.error("定时执行订单收货！", e);
        }
    }
}