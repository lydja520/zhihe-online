package cn.com.zhihetech.online.bean;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ShenYunjie on 2016/4/25.
 */
@Entity
@Table(name = "t_message_ext")
public class MessageExt extends SerializableAndCloneable {
    private String extId;
    private ChatMessage message;
    private String key;
    private String value;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "ext_id", length = 36)
    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    @JSONField(serialize = false)
    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    @Column(name = "ext_key", nullable = false, length = 100)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "ext_value", length = 500)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
