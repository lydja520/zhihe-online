package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.MessageBody;
import cn.com.zhihetech.online.dao.IMessageBodyDao;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
@Repository("messageBodyDao")
public class MessageBodyDaoImpl extends SimpleSupportDao<MessageBody> implements IMessageBodyDao {

    @Override
    public void updateMessageBodyMsg(String bodyId, String msgBody) {
        String sql = "update t_message_body set body_msg = :bodyMsg where body_id = :bodyId";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("bodyMsg", "11111111111111111");
        query.setParameter("bodyId", bodyId);
        int total = query.executeUpdate();
        System.out.println(total);
    }
}
