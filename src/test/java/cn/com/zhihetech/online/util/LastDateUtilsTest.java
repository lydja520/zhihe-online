package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.vo.StartDateAndEndDate;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/5/9.
 */
public class LastDateUtilsTest {

    @Test
    public void testLastWeek() throws Exception {
        StartDateAndEndDate startDateAndEndDate = DateUtils.lastWeek();
        System.out.println(startDateAndEndDate);
    }

    @Test
    public void testThisWeek() throws Exception {
        StartDateAndEndDate startDateAndEndDate = DateUtils.thisWeek();
        System.out.println(startDateAndEndDate);
    }
}