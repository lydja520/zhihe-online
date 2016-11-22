package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ChatMessage;
import cn.com.zhihetech.online.bean.MessageBody;
import cn.com.zhihetech.online.bean.MessagePayload;
import cn.com.zhihetech.online.dao.IChatMessageDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.AbstractService;
import cn.com.zhihetech.online.service.IChatMessageService;
import cn.com.zhihetech.online.service.IMessageBodyService;
import cn.com.zhihetech.online.service.IMessageExtService;
import cn.com.zhihetech.online.util.ChatMessageResult;
import cn.com.zhihetech.online.util.EMChatUtils;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
@Service("chatMessageService")
public class ChatMessageServiceImpl extends AbstractService<ChatMessage> implements IChatMessageService {

    @Resource(name = "chatMessageDao")
    private IChatMessageDao chatMessageDao;
    @Resource(name = "messageBodyService")
    private IMessageBodyService messageBodyService;
    @Resource(name = "messageExtService")
    private IMessageExtService messageExtService;


    private long waitTimestamp = 0l;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public ChatMessage getById(java.lang.String id) {
        return this.chatMessageDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param chatMessage 需要删除的持久化对象
     */
    @Override
    public void delete(ChatMessage chatMessage) {

    }

    /**
     * 添加一个对象到数据库
     *
     * @param chatMessage 需要持久化的对象
     * @return
     */
    @Override
    public ChatMessage add(ChatMessage chatMessage) {
        return this.chatMessageDao.saveEntity(chatMessage);
    }

    /**
     * 更新一个持久化对象
     *
     * @param chatMessage
     */
    @Override
    public void update(ChatMessage chatMessage) {
        this.chatMessageDao.updateEntity(chatMessage);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<ChatMessage> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.chatMessageDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<ChatMessage> getPageData(Pager pager, IQueryParams queryParams) {
        return this.chatMessageDao.getPageData(pager, queryParams);
    }

    @Override
    public void executeImportMessages(List<ChatMessage> entities) {
        for (ChatMessage message : entities) {
            message.setCreateDate(new Date());
            this.chatMessageDao.saveEntity(message);
            MessagePayload payload = message.getPayload();
            if(payload == null){
                continue;
            }
            this.messageExtService.executeBatchAdd(payload.getMsgExts(message));   //批量添加消息扩展属性
            List<MessageBody> messageBodies = payload.getBodies();
            if (messageBodies == null || messageBodies.size() < 1) {
                continue;
            }
            for (MessageBody messageBody : messageBodies) {
                messageBody.setMessage(message);
                this.messageBodyService.add(messageBody);
            }
        }
    }


    @Override
    public Long getLastTimestamp() {
        List<Object> list = this.chatMessageDao.getProperty("max(timestamp)", null, null);
        if (list != null && list.size() > 0) {
            if (list.get(0) == null) {
                return null;
            }
            return Long.parseLong(java.lang.String.valueOf(list.get(0)));
        }
        return null;
    }

    @Override
    public ChatMessage getLastChatMessage() {
        IQueryParams queryParams = new GeneralQueryParams().sort("msg_id", Order.DESC);
        Pager pager = new Pager(1, 1);
        List<ChatMessage> messages = this.chatMessageDao.getEntities(pager, queryParams);
        if (messages.size() > 0) {
            return messages.get(0);
        }
        return null;
    }

    /**
     * 获取聊天记录
     *
     * @param msgId //指定消息ID
     * @param to    //接收此消息的目标ID（群或聊天室为群ID或聊天室ID,单聊为用户ID)
     * @param from  //消息发送用户的ID(如果为查询群聊此参数必须为空）
     * @return
     */
    @Override
    public PageData<ChatMessage> getBeforeTimestampHistoryMessages(String msgId, java.lang.String to, java.lang.String from) {
        if (StringUtils.isEmpty(to)) {
            throw new SystemException("request params is not able null");
        }
        if (StringUtils.isEmpty(msgId)) {
            ChatMessage message = getLastChatMessage();
            if (message == null) {
                return new PageData<>(0, new Pager(1, 20));
            }
            msgId = message.getMsg_id();
        }

        IQueryParams queryParams = new GeneralQueryParams().andLessThan("msg_id", msgId).sort("msg_id", Order.DESC);
        if (cn.com.zhihetech.util.common.StringUtils.isEmpty(from)) {
            queryParams.andEqual("chat_type", ChatMessage.ChatType.groupchat).andEqual("to", to);
        } else {
            List<Object> values = new ArrayList<>();
            values.add(from);
            values.add(to);
            queryParams.andIn("sender", values).andIn("to", values)
                    .andEqual("chat_type", ChatMessage.ChatType.chat);
        }
        PageData<ChatMessage> pageData = this.getPageData(new Pager(), queryParams);
        for (ChatMessage message : pageData.getRows()) {
            MessagePayload payload = new MessagePayload();
            payload.setBodies(this.messageBodyService.getMessageBodiesByMessageId(message.getMessageId()));
            payload.setExt(this.messageExtService.getMessageExtByMessageId(message.getMessageId()));
            message.setPayload(payload);
        }
        return pageData;
    }

    @Override
    public void exportMessagesAlways() {
        if (waitTimestamp > 0) {
            try {
                Thread.sleep(waitTimestamp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitTimestamp = 0l;
        }
        long startDateTime = System.currentTimeMillis();
        EMChatUtils messageUtils = new EMChatUtils();
        Long lastTimestamp = this.getLastTimestamp();
        Long currentTimestamp = System.currentTimeMillis();
        ChatMessageResult messageResult = messageUtils.exportChatMessages(lastTimestamp, currentTimestamp, ChatMessageResult.MESSAGE_PAGE_SIZE, null);
        if (messageResult.getStatusCode() == 200 && messageResult.getCount() > 0) {
            this.executeImportMessages(messageResult.getEntities());
        }
        while (messageResult.getStatusCode() == 200 && messageResult.getCount() >= ChatMessageResult.MESSAGE_PAGE_SIZE) {
            long startTimestamp = System.currentTimeMillis();
            messageResult = messageUtils.exportChatMessages(lastTimestamp, currentTimestamp, ChatMessageResult.MESSAGE_PAGE_SIZE, messageResult.getCursor());
            if (messageResult.getStatusCode() == 200 && messageResult.getCount() > 0) {
                this.executeImportMessages(messageResult.getEntities());
            }
            long endTimestamp = System.currentTimeMillis();
            long duration = 1000 - endTimestamp - startTimestamp;
            if (duration > 0) {
                try {
                    Thread.sleep(duration);
                } catch (Exception e) {
                }
            }
        }
        long endDateTime = System.currentTimeMillis();
        this.waitTimestamp = 1000 - endDateTime - startDateTime;
    }
}
