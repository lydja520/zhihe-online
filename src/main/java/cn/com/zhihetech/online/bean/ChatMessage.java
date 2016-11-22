package cn.com.zhihetech.online.bean;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ShenYunjie on 2016/4/25.
 */
@Entity
@Table(name = "t_chat_message")
public class ChatMessage extends SerializableAndCloneable {

    public enum ChatType {
        chat, groupchat, chatroom   //群聊（包括聊天室）
    }

    public enum MessageType {
        txt, img, audio, loc, video, cmd   //视频类型消息
    }

    private String messageId;   //自定义聊天记录ID
    private String type;
    //private String from; //发送人username
    private String sender; //发送人username
    private String msg_id; //消息id
    private ChatType chat_type;//用来判断单聊还是群聊。chat:单聊，groupchat:群聊
    private long timestamp; //消息发送时间
    private String to;//接收人的username或者接收group的id
    private Date createDate;//业务服务器保存时间

    private MessagePayload payload;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "chat_message_id", length = 36)
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Column(name = "type", length = 50)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JSONField(name = "from")
    @Column(name = "message_from", nullable = false, length = 50)
    public String getSender() {
        return sender;
    }

    @JSONField(name = "from")
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Column(name = "em_msg_id", nullable = false, length = 50, unique = true)
    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    @Enumerated(value = EnumType.STRING)
    @Column(name = "chat_type", nullable = false, length = 15)
    public ChatType getChat_type() {
        return chat_type;
    }

    public void setChat_type(ChatType chat_type) {
        this.chat_type = chat_type;
    }

    @Column(name = "time_stamp", nullable = false)
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "em_msg_to", nullable = false, length = 50)
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Column(name = "create_date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Transient
    public MessagePayload getPayload() {
        return payload;
    }

    @JSONField(deserialize = false)
    public void setPayload(MessagePayload payload) {
        this.payload = payload;
    }
}
