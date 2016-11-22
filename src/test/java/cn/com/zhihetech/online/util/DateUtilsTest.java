package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.vo.StartDateAndEndDate;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/5/10.
 */
public class DateUtilsTest {

    @Test
    public void testDate() throws Exception {
        String dateStr = "2016-5-9 00:00:01";
        Date date = DateUtils.String2Date(dateStr);
        StartDateAndEndDate startDateAndEndDate = DateUtils.lastWeek();
        StartDateAndEndDate startDateAndEndDate1 = DateUtils.thisWeek();
        int i = DateUtils.dayOFWeek(date);
        System.out.println("============");
    }

    @Test
    public void testThisDay() throws Exception {
        StartDateAndEndDate startDateAndEndDate = DateUtils.thisDay();
        System.out.println(startDateAndEndDate);
    }

    @Test
    public void testTime() {
        Date endDate = DateUtils.String2DateTime("2016-6-5 18:00:00");
        long endTime = endDate.getTime();
        System.out.println(endTime);
    }

    @Test
    public void testFormatDate() throws Exception {
        String tmp = DateUtils.formatDate(new Date(),"yyyyMMdd");
        System.out.print(tmp);
    }
}