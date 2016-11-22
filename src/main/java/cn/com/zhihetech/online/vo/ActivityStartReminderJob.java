package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;

import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/5/23.
 */
public class ActivityStartReminderJob extends SerializableAndCloneable{

    private List<String> phoneNumbers;

    private Date startDate;

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
