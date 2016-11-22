package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ChatMessage;
import cn.com.zhihetech.online.dao.IChatMessageDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
@Repository("chatMessageDao")
public class ChatMessageDaoImpl extends SimpleSupportDao<ChatMessage> implements IChatMessageDao {
}
