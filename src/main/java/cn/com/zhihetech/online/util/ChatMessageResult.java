package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.bean.ChatMessage;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
public class ChatMessageResult {

    public static final int MESSAGE_PAGE_SIZE = 200;

    private long count;
    private String cursor;
    private int statusCode;
    private long timestamp;
    private long duration;
    private List<ChatMessage> entities;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<ChatMessage> getEntities() {
        return entities;
    }

    public void setEntities(List<ChatMessage> entities) {
        this.entities = entities;
    }
}
