package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.commons.WebChineseConfig;
import cn.com.zhihetech.online.service.IUserService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.text.MessageFormat;
import java.util.List;


/**
 * Created by YangDaiChun on 2016/6/3.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SMSUtilsTest {

    @Resource(name = "userService")
    private IUserService userService;

    @Test
    public void testSendSMS() throws Exception {
        String smsText = "尊敬的用户，您好！您关注的商家将在30分钟后16:60举办实淘会客厅，您心仪的商品将会进行低价秒杀，更有现金红包等你来抢！请准时参加哦~【实淘】，退订回复TD";
        SMSUtils.sendSMS("18788520885,18687317688", smsText);
    }

    @Test
    public void sendGroupSMS() {
        //new User().getUserPhone()
        int page = 1;
        int size = 100;
        String smsText = "今日14：30实淘会客厅，1元购茶砖、6元秒生态红米，全包邮。还有更多超值好货，全情礼献。现在锁定实淘会客厅，玩转全新抢货体验。退订回复TD【实淘】";
        IQueryParams queryParams = new GeneralQueryParams()
                .sort("createDate", Order.ASC);
        List<Object> userMobiles;
        do {
            userMobiles = this.userService.getProperty("userPhone", new Pager(page, size), queryParams);
            if (userMobiles != null && !userMobiles.isEmpty()) {
                String nums = "";
                for (Object obj : userMobiles) {
                    nums += obj + ",";
                }
                nums = StringUtils.isEmpty(nums) ? nums : nums.substring(0, nums.length() - 1);
                boolean success = SMSUtils.sendSMS(nums, smsText);
                System.out.println(nums);
            }
            page++;
        } while (userMobiles != null && userMobiles.size() >= size && page < 20);
    }

    @Test
    public void testSMSRefundOk() {
        String smsText = MessageFormat.format(WebChineseConfig.MsgTemplate.ORDER_REFUND_OK, "123456");
        SMSUtils.sendSMS("18788520885", smsText);
        System.out.println("发送成功！");
    }
}