package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.OrderExamine;
import cn.com.zhihetech.online.dao.IOrderExamineDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/6/29.
 */
@Repository("orderExamineDao")
public class OrderExamineDaoImpl extends SimpleSupportDao<OrderExamine> implements IOrderExamineDao {
}
