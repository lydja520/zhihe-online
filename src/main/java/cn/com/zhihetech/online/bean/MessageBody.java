package cn.com.zhihetech.online.bean;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ShenYunjie on 2016/4/25.
 */
@Entity
@Table(name = "t_message_body")
public class MessageBody extends SerializableAndCloneable {

    private String bodyId;
    private ChatMessage message;
    private ChatMessage.MessageType type = ChatMessage.MessageType.txt;//消息类型。txt:文本消息, img:图片, loc：位置, audio：语音
    private String msg;//消息内容
    private float length;//语音时长，单位为秒，这个属性只有语音消息有
    private String url;//图片语音等文件的网络url，图片和语音消息有这个属性
    private String filename;//文件名字，图片和语音消息有这个属性
    private String secret;//获取文件的secret，图片和语音消息有这个属性
    private float lat;//发送的位置的纬度，只有位置消息有这个属性
    private float lng;//位置经度，只有位置消息有这个属性
    private String addr; //位置消息详细地址，只有位置消息有这个属性
    private String thumb;   //缩略图地址
    private String thumb_secret;    //上传缩略图后返回

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "body_id", length = 36)
    public String getBodyId() {
        return bodyId;
    }

    public void setBodyId(String bodyId) {
        this.bodyId = bodyId;
    }

    @JSONField(serialize = false)
    @ManyToOne
    @JoinColumn(nullable = false, name = "message_id")
    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    @Enumerated(value = EnumType.STRING)
    @Column(name = "msg_type", nullable = false)
    public ChatMessage.MessageType getType() {
        return type;
    }

    public void setType(ChatMessage.MessageType type) {
        this.type = type;
    }

    @Column(name = "body_msg", length = 500)
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Column(name = "body_length")
    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    @Column(name = "body_url", length = 300)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "file_name", length = 300)
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Column(name = "body_secret", length = 300)
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Column(name = "loc_lat")
    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    @Column(name = "loc_lng")
    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    @Column(name = "loc_addr", length = 400)
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Column(name = "img_thumb_url", length = 200)
    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Column(name = "thumb_secret", length = 100)
    public String getThumb_secret() {
        return thumb_secret;
    }

    public void setThumb_secret(String thumb_secret) {
        this.thumb_secret = thumb_secret;
    }
}
