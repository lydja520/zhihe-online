package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.MessageBody;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
public interface IMessageBodyService extends UpgradedService<MessageBody> {
    /**
     * 根据Message ID获取MessageBody
     *
     * @param messageId
     * @return
     */
    List<MessageBody> getMessageBodiesByMessageId(CharSequence messageId);

    /**
     * 根据MessageBody ID更新msgBody
     *
     * @param bodyId
     * @param msgBody
     */
    void updateMessageBodyMsg(String bodyId, String msgBody);
}
