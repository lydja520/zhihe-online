package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.MessageBody;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
public interface IMessageBodyDao extends SupportDao<MessageBody> {

    /**
     * 用户单独更新消息内容，Emoji表情用hibernate无法存储
     *
     * @param bodyId
     * @param msgBody
     */
    void updateMessageBodyMsg(String bodyId, String msgBody);
}
