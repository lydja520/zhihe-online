package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.ChatMessage;
import cn.com.zhihetech.online.service.IChatMessageService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
@Controller
public class ChatMessageApiController extends ApiController {

    @Resource(name = "chatMessageService")
    private IChatMessageService chatMessageService;

    /**
     * URL:api/chatmessages
     *
     * @param msgId //获取此消息ID之前的聊天记录
     * @param to    //信息接受者（单聊为用户环信ID,群聊或聊天室为群ID或聊天室ID)，此参数不能为空
     * @param from  //如果是获取单聊聊天记录则专递此参数，此参数为自己对应的环信ID
     * @return
     */
    @ResponseBody
    @RequestMapping("chatmessages")
    public PageData<ChatMessage> getChatMessages(String msgId, String to, String from) {
        this.chatMessageService.exportMessagesAlways();
        return this.chatMessageService.getBeforeTimestampHistoryMessages(msgId, to, from);
    }
}
