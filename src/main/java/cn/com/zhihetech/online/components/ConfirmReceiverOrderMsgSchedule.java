package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.WebChineseConfig;
import cn.com.zhihetech.online.service.IOrderDetailService;
import cn.com.zhihetech.online.service.IOrderScheduleService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.SMSUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;

import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by ydc on 16-6-24.
 */
public class ConfirmReceiverOrderMsgSchedule implements Job {

    private IOrderScheduleService orderScheduleService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try {
            SchedulerContext skedCtx = jobExecutionContext.getScheduler().getContext();
            orderScheduleService = (IOrderScheduleService) skedCtx.get("orderScheduleService");
            String orderId = (String) jobExecutionContext.getMergedJobDataMap().get("orderId");
            Map<String, Object> orderStateAndPhoneNum = this.orderScheduleService.getOrderState(orderId);
            int orderState = (int) orderStateAndPhoneNum.get("orderState");
            if (orderState != Constant.ORDER_STATE_ALREADY_DISPATCHER) {
                return;
            }
            String phoneNum = (String) orderStateAndPhoneNum.get("phoneNum");
            String orderCode = (String) orderStateAndPhoneNum.get("orderCode");
            String smsText = MessageFormat.format(WebChineseConfig.MsgTemplate.ORDER_CONFIRM_RECEIVE, orderCode);
            SMSUtils.sendSMS(phoneNum, smsText);
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setRefireImmediately(true);
            Log log = LogFactory.getLog(this.getClass());
            log.error("发送提醒两天后将会进行确认收货信息失败", e);
        }
    }
}
