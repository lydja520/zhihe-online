package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.commons.WebChineseConfig;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.SMSUtils;
import cn.com.zhihetech.online.vo.ActivityStartReminderJob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/5/23.
 */
@Component(value = "activityStartSmsReminder")
public class ActivityStartSmsReminder implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Object scheduleJob = context.getMergedJobDataMap().get("activityStartReminderJob");
            ActivityStartReminderJob activityStartReminderJob = (ActivityStartReminderJob) scheduleJob;
            List<String> phoneNumbers = activityStartReminderJob.getPhoneNumbers();
            StringBuffer phoneNumTemp = new StringBuffer("");
            for (int i = 0; i < phoneNumbers.size(); i++) {
                String phoneNumber = phoneNumbers.get(i);
                if (i == 0) {
                    phoneNumTemp.append(phoneNumber);
                    continue;
                }
                phoneNumTemp.append(",").append(phoneNumber);
            }
            String smsText = MessageFormat.format(WebChineseConfig.MsgTemplate.ACTIVITY_START_REMINDER, DateUtils.formatDateTime(activityStartReminderJob.getStartDate()));
            SMSUtils.sendSMS(phoneNumTemp.toString(), smsText);
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            Log log = LogFactory.getLog(this.getClass());
            log.error("发送提醒用户活动开始短信失败", e);
        }
    }
}
