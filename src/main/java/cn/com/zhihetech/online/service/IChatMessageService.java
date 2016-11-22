package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ChatMessage;
import cn.com.zhihetech.util.hibernate.commons.PageData;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
public interface IChatMessageService extends UpgradedService<ChatMessage> {
    /**
     * 将消息导入数据库
     *
     * @param entities
     */
    void executeImportMessages(List<ChatMessage> entities);

    /**
     * 获取最新的聊天记录时间戳
     *
     * @return
     */
    Long getLastTimestamp();

    /**
     * 获取最新的一条聊天记录
     *
     * @return
     */
    ChatMessage getLastChatMessage();

    /**
     * 获取指定消息ID之前的聊天记录
     *
     * @param msgId //指定消息ID
     * @param to    //接收此消息的目标ID（群或聊天室为群ID或聊天室ID,单聊为用户ID)
     * @param from  //消息发送用户的ID(如果为查询群聊此参数必须为空）
     * @return
     */
    PageData<ChatMessage> getBeforeTimestampHistoryMessages(String msgId, String to, String from);

    /**
     * 先从环信服务器导出聊天记录到本地服务器
     */
    void exportMessagesAlways();
}
