package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.dao.IOrderDetailDao;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YangDaiChun on 2016/1/5.
 */
@Repository(value = "orderDetailDao")
public class OrderDetailDaoImpl extends SimpleSupportDao<OrderDetail> implements IOrderDetailDao {

}
