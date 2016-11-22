package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.OrderEditRecord;
import cn.com.zhihetech.online.dao.IOrderDao;
import cn.com.zhihetech.online.dao.IOrderEditRecordDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/5/12.
 */
@Repository(value = "orderRecordDao")
public class OrderEditRecordDaoImpl extends SimpleSupportDao<OrderEditRecord> implements IOrderEditRecordDao {
}
