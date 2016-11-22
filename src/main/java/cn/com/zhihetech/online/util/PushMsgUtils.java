package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.exception.SystemException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

/**
 * Created by YangDaiChun on 2016/3/28.
 */
public class PushMsgUtils {

    /**
     * 向指定用户推送消息
     *
     * @param userId
     * @param notification
     */
    public static void PushToUserByUserId(String userId, String notification) {
        String alias = userId.replaceAll("-", "");
        JPushClient jpushClient = new JPushClient(AppConfig.JPushConfig.MASTER_SECRET, AppConfig.JPushConfig.APP_KEY, AppConfig.JPushConfig.MAX_RETRY_TIMES);
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(notification))
                .build();
        sendPush(jpushClient, payload);
    }

    private static void sendPush(JPushClient jpushClient, PushPayload payload) {
        PushResult result = null;
        try {
            result = jpushClient.sendPush(payload);
            System.out.print(result);
        } catch (APIConnectionException e) {
            throw new SystemException("系统出错，请与管理员联系");
        } catch (APIRequestException e) {
            throw new SystemException("系统出错，请与管理员联系");
        }
        if (result != null && result.getResponseCode() == 200) {
            return;
        } else {
            throw new SystemException("推送失败");
        }
    }

}
