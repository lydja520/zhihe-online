package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.service.IMerchantBillService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.vo.StartDateAndEndDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by ShenYunjie on 2016/6/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MerchantBillServiceImplTest {
    @Test
    public void saveMerchantBills() throws Exception {

    }

    @Resource(name = "merchantBillService")
    private IMerchantBillService merchantBillService;

    @Test
    public void testSaveMerchantBills() throws Exception {
        StartDateAndEndDate lastWeekStartDateAndEndDate = DateUtils.lastWeek();
        Date lastWeekStartDate = DateUtils.String2DateTime("2016-06-18 00:00:00");
        Date lastWeekEndDate = lastWeekStartDateAndEndDate.getEndDate();
        this.merchantBillService.saveMerchantBills(null, lastWeekStartDate, lastWeekEndDate);
    }
}