package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.bean.ChatMessage;
import cn.com.zhihetech.online.bean.MessageBody;
import cn.com.zhihetech.online.service.IChatMessageService;
import cn.com.zhihetech.online.service.IMessageBodyService;
import cn.com.zhihetech.online.util.ChatMessageResult;
import cn.com.zhihetech.online.util.EMChatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ChatMessageComponentTest {

    @Resource(name = "chatMessageService")
    private IChatMessageService chatMessageService;
    @Resource(name = "messageBodyService")
    private IMessageBodyService messageBodyService;

    @Test
    public void exportChatMessages() throws Exception {
        long startDate = System.currentTimeMillis();
        ChatMessageResult messageResult = new EMChatUtils().exportChatMessages(null, new Date().getTime(), 200, null);
        if (messageResult.getStatusCode() == 200 && messageResult.getCount() > 0) {
            this.chatMessageService.executeImportMessages(messageResult.getEntities());
        }
        long endDate = System.currentTimeMillis();

        double tmp = ((double) (endDate - startDate)) / 1000;
        double _tmp = ((double) (endDate - startDate)) / 1000;
    }

    @Test
    public void getMessages() {
        List<ChatMessage> messages = this.chatMessageService.getAllByParams(null, null);
        for (ChatMessage message : messages) {
            List<MessageBody> messageBodies = this.messageBodyService.getMessageBodiesByMessageId(message.getMessageId());
            for (MessageBody body : messageBodies) {
                System.out.println(body.getMsg());
            }
        }
    }
}