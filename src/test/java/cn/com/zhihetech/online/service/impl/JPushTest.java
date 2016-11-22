package cn.com.zhihetech.online.service.impl;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.DeviceType;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/22.
 */
public class JPushTest {

    @Test
    public void test() {
        String tmp = String.valueOf(new Date().getTime());
        System.out.println(tmp);
    }

    @Test
    public void pushTest() {
        JPushClient jpushClient = new JPushClient("bd987cad7712b455b8f9a392", "52f157b4b71c12e466d4d2ec", 5);
        PushPayload payload = buildPushObject_all_all_alert();

        try {
            PushResult result = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    public PushPayload buildPushObject_all_all_alert() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias("078ef16333fc4b9fab051d7948ba77f5"))
                .setNotification(Notification.alert("ydc测试"))
                .build();
    }
}
