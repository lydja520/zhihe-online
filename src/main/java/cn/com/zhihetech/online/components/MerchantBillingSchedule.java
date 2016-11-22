package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.service.IMerchantBillService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.vo.StartDateAndEndDate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
@Component
public class MerchantBillingSchedule extends QuartzJobBean implements StatefulJob {

    private IMerchantBillService merchantBillService;

    /**
     * 定时对商家的账单进行结算
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            SchedulerContext skedCtx = jobExecutionContext.getScheduler().getContext();
            merchantBillService = (IMerchantBillService) skedCtx.get("merchantBillService");
            StartDateAndEndDate lastWeekStartDateAndEndDate = DateUtils.lastWeek();
            Date lastWeekStartDate = lastWeekStartDateAndEndDate.getStartDate();
            Date lastWeekEndDate = lastWeekStartDateAndEndDate.getEndDate();
            this.merchantBillService.saveMerchantBills(null, lastWeekStartDate, lastWeekEndDate);
        } catch (SchedulerException e) {
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setRefireImmediately(true);
            Log log = LogFactory.getLog(this.getClass());
            log.error("执行商家账单结算定时任务失败！",e);
        }
    }
}
