package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.service.IMerchantBillService;
import cn.com.zhihetech.online.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/8/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MerchantBillingScheduleTest {

    @Resource(name = "merchantBillService")
    private IMerchantBillService merchantBillService;

    @Test
    public void addMerchantBill() {
        this.merchantBillService.saveMerchantBills(null, DateUtils.String2DateTime("2016-06-01 00:00:00"), new Date());
    }

}