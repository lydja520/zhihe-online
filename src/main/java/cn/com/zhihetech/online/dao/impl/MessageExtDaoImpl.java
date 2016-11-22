package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.MessageExt;
import cn.com.zhihetech.online.dao.IMessageExtDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
@Repository("messageExtDao")
public class MessageExtDaoImpl extends SimpleSupportDao<MessageExt> implements IMessageExtDao {
}
