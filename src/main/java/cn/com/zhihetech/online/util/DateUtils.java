package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.vo.StartDateAndEndDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ShenYunjie on 2015/12/18.
 */
public class DateUtils {

    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        return sdf.format(date);
    }

    public static Date String2Date(String target) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(target);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date String2DateTime(String target) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            return sdf.parse(target);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定日期的开始时间；如2015-05-04开始时间为：2015-05-4 00:00:00
     *
     * @return
     */
    public static Date getStartDateTimeWithDate(Date target) {
        return String2Date(formatDate(target));
    }

    /**
     * 获取指定日期的结束时间；如2015-05-04结束时间为：2015-05-4 23:59:59
     *
     * @param target
     * @return
     */
    public static Date getEndDateTimeWithDate(Date target) {
        String tmp = formatDate(target) + " 23:59:59";
        return String2DateTime(tmp);
    }

    /**
     * 获取当前时间的上个周的起止时间（此方法适用于一周的第一天是星期一）
     *
     * @param
     * @return
     */
    public static StartDateAndEndDate lastWeek() {
        StartDateAndEndDate startDateAndEndDate = new StartDateAndEndDate();

        Calendar nowCal = Calendar.getInstance();
        Calendar nowCal1 = (Calendar) nowCal.clone();
        Calendar nowCal2 = (Calendar) nowCal.clone();
        // 一周第一天是否为星期天
        boolean isFirstSunday = (nowCal.getFirstDayOfWeek() == Calendar.SUNDAY);
        // 获取周几
        int dayOfWeek = nowCal.get(Calendar.DAY_OF_WEEK);
        // 若一周第一天为星期天，则-1
        if (isFirstSunday) {
            dayOfWeek = dayOfWeek - 1;
            if (dayOfWeek == 0) {
                dayOfWeek = 7;
            }
        }
        nowCal1.add(Calendar.DATE, -dayOfWeek - 6);
        Date startDate = nowCal1.getTime();
        nowCal2.add(Calendar.DATE, -dayOfWeek);
        Date endDate = nowCal2.getTime();
        startDate = String2DateTime(formatDate(startDate) + " 00:00:00");
        endDate = String2DateTime(formatDate(endDate) + " 23:59:59");

        startDateAndEndDate.setStartDate(startDate);
        startDateAndEndDate.setEndDate(endDate);
        return startDateAndEndDate;
    }

    /**
     * 获取指定日期date本周的起止时间
     *
     * @return
     */
    public static StartDateAndEndDate thisWeek() {
        StartDateAndEndDate startDateAndEndDate = new StartDateAndEndDate();
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2;
        cal.add(Calendar.DATE, -dayOfWeek);
        Date startDate = cal.getTime();
        startDate = DateUtils.String2DateTime(DateUtils.formatDate(startDate) + " 00:00:00");
        cal.add(Calendar.DATE, 6);
        Date endDate = cal.getTime();
        endDate = DateUtils.String2DateTime(DateUtils.formatDate(endDate) + " 23:59:59");

        startDateAndEndDate.setStartDate(startDate);
        startDateAndEndDate.setEndDate(endDate);
        return startDateAndEndDate;
    }

    public static int dayOFWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static StartDateAndEndDate thisDay() {
        StartDateAndEndDate startDateAndEndDate = new StartDateAndEndDate();
        Date now = new Date();
        String nowDate = formatDate(now);
        String startDateStr = nowDate + " 00:00:00";
        String endDateStr = nowDate + " 23:59:59";
        Date startDate = String2DateTime(startDateStr);
        Date endDate = String2DateTime(endDateStr);
        startDateAndEndDate.setStartDate(startDate);
        startDateAndEndDate.setEndDate(endDate);
        return startDateAndEndDate;
    }

    /**
     * 获取自定义格式化时间字符串
     *
     * @param target
     * @param format
     * @return
     */
    public static String formatDate(Date target, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(target);
    }
}
