package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.service.IChatMessageService;
import cn.com.zhihetech.online.util.ChatMessageResult;
import cn.com.zhihetech.online.util.EMChatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by ShenYunjie on 2016/4/25.
 */
@Component
public class ChatMessageComponent {

    private final static Log log = LogFactory.getLog(ChatMessageComponent.class);

    @Resource(name = "chatMessageService")
    private IChatMessageService chatMessageService;

    private final int PAGE_SIZE = ChatMessageResult.MESSAGE_PAGE_SIZE;   //导出聊天记录分页大小，每页200条

    private EMChatUtils messageUtils;

    /**
     * 初始化方法，初始化之后即可自动导入此刻之前的所有聊天记录，并触发定时自动导入方法
     */
    @PostConstruct
    public void exportOldMessagesAndExecuteTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    exportChatMessageFromEMChatServer();
                } catch (Exception ex) {
                    log.error("导入环信聊天记录失败！", ex);
                }
            }
        }).start();
        //exportOldMessagesAndExecuteTimer();
    }

    private void exportChatMessageFromEMChatServer() {
        messageUtils = new EMChatUtils();
        Long lastTimestamp = this.chatMessageService.getLastTimestamp();
        Long currentTimestamp = System.currentTimeMillis();
        ChatMessageResult messageResult = messageUtils.exportChatMessages(lastTimestamp, currentTimestamp, PAGE_SIZE, null);
        if (messageResult.getStatusCode() == 200 && messageResult.getCount() > 0) {
            this.chatMessageService.executeImportMessages(messageResult.getEntities());
        }
        while (messageResult.getStatusCode() == 200 && messageResult.getCount() >= PAGE_SIZE) {
            long startTimestamp = System.currentTimeMillis();
            messageResult = messageUtils.exportChatMessages(lastTimestamp, currentTimestamp, PAGE_SIZE, messageResult.getCursor());
            if (messageResult.getStatusCode() == 200 && messageResult.getCount() > 0) {
                this.chatMessageService.executeImportMessages(messageResult.getEntities());
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
    }
}
