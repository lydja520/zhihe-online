package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

import java.util.Date;

/**
 * Created by ShenYunjie on 2015/12/14.
 */
public interface IRedEnvelopItemServiceDao extends SupportDao<RedEnvelopItem> {
    int executeGrab(String redEnvelopId, String userId, Date date);
}
