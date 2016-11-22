package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.vo.ExportOrder;
import cn.com.zhihetech.online.vo.OrderSearch;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/1/4.
 */
public interface IOrderDao extends SupportDao<Order> {

    List<ExportOrder> getExportOrderByMerchantId(String merchantId, OrderSearch orderSearch);
}
