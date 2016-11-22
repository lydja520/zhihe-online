package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.dao.IOrderDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.PropertiesUtils;
import cn.com.zhihetech.online.vo.ExportOrder;
import cn.com.zhihetech.online.vo.OrderSearch;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import com.alibaba.fastjson.serializer.CalendarCodec;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/1/4.
 */
@Repository(value = "orderDao")
public class OrderDaoImpl extends SimpleSupportDao<Order> implements IOrderDao {

    @Override
    public List<ExportOrder> getExportOrderByMerchantId(String merchantId, OrderSearch orderSearch) {


        StringBuffer sql1 = new StringBuffer("SELECT ")
                .append("t_order.create_date,t_order.order_code,t_order.order_state,t_merchant.merch_name,t_merchant.merch_tell," +
                        "t_merchant.mobile_no,t_user.user_phone ,t_order.receiver_name,t_order.receiver_phone," +
                        "t_order.receiver_add,t_goods.goods_name,t_order_detail.sku_name,t_order_detail.goods_count")
                .append(" FROM ")
                .append(" t_order,t_merchant ,t_user,t_order_detail,t_goods ")
                .append(" WHERE ")
                .append("t_order.merchant_id = t_merchant.merch_id")
                .append(" AND ")
                .append("t_order.user_id = t_user.user_id")
                .append(" AND ")
                .append("t_order.order_id = t_order_detail.order_id")
                .append(" AND ")
                .append("t_goods.goods_id = t_order_detail.goods_id ")
                .append(" AND ")
                .append("t_order.merchant_id = t_merchant.merch_id")
                .append(" AND ")
                .append("t_order.delete_state=FALSE");
        if (!StringUtils.isEmpty(merchantId)) {
            sql1.append(" AND ")
                    .append("t_merchant.merch_id=:merchantId");
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchOrderCode())) {
            sql1.append(" AND ")
                    .append("t_order.order_code=:orderCode");
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchOrderState())) {
            sql1.append(" AND ")
                    .append("t_order.order_state=:orderState");
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchMerchantName())) {
            sql1.append("  AND ")
                    .append("t_merchant.merch_name LIKE :merchantName");
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchUserPhone())) {
            sql1.append(" AND ")
                    .append("t_user.user_phone=:userPhone");
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchStartTime()) || !StringUtils.isEmpty(orderSearch.getSearchEndTime())) {
            if (!StringUtils.isEmpty(orderSearch.getSearchStartTime())) {
                sql1.append(" AND ")
                        .append("t_order.create_date>=:startTime");
            }
            if (!StringUtils.isEmpty(orderSearch.getSearchEndTime())) {
                sql1.append(" AND ")
                        .append("t_order.create_date<=:endTime");
            }
        }
        sql1.append(" ORDER BY ")
                .append("t_order.create_date,t_order.order_code,t_merchant.merch_name");
        StringBuffer sql2 = new StringBuffer("SELECT COUNT(*) FROM(").append(sql1).append(") AS a");
        Session session = this.getSession();
        Query query = session.createSQLQuery(sql2.toString());
        if (!StringUtils.isEmpty(merchantId)) {
            query.setString("merchantId", merchantId);
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchOrderCode())) {
            query.setString("orderCode", orderSearch.getSearchOrderCode());
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchOrderState())) {
            query.setInteger("orderState", Integer.parseInt(orderSearch.getSearchOrderState()));
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchMerchantName())) {
            query.setString("merchantName", "%" + orderSearch.getSearchMerchantName() + "%");
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchUserPhone())) {
            query.setString("userPhone", orderSearch.getSearchUserPhone());
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchStartTime()) || !StringUtils.isEmpty(orderSearch.getSearchEndTime())) {
            if (!StringUtils.isEmpty(orderSearch.getSearchStartTime())) {
                query.setTimestamp("startTime", DateUtils.String2DateTime(orderSearch.getSearchStartTime()));
            }
            if (!StringUtils.isEmpty(orderSearch.getSearchEndTime())) {
                query.setTimestamp("endTime", DateUtils.String2Date(orderSearch.getSearchEndTime()));
            }
        }
        BigInteger _count = (BigInteger) query.uniqueResult();  //查询满足当前查询条件的数据条数
        int count = _count.intValue();
        int maxCount = Integer.parseInt(PropertiesUtils.getProperties().getProperty("EXPORT_DATA_MAX_RECORD"));  //生成excel表格最大允许的条数
        if (count > maxCount) {
            throw new SystemException("生成excel失败！原因：根据当前查询条件查询出来的数据太多，无法无法生成excel表格！");
        }
        query = session.createSQLQuery(sql1.toString());
        if (!StringUtils.isEmpty(merchantId)) {
            query.setString("merchantId", merchantId);
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchOrderCode())) {
            query.setString("orderCode", orderSearch.getSearchOrderCode());
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchOrderState())) {
            query.setInteger("orderState", Integer.parseInt(orderSearch.getSearchOrderState()));
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchMerchantName())) {
            query.setString("merchantName", "%" + orderSearch.getSearchMerchantName() + "%");
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchUserPhone())) {
            query.setString("userPhone", orderSearch.getSearchUserPhone());
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchStartTime()) || !StringUtils.isEmpty(orderSearch.getSearchEndTime())) {
            if (!StringUtils.isEmpty(orderSearch.getSearchStartTime())) {
                query.setTimestamp("startTime", DateUtils.String2DateTime(orderSearch.getSearchStartTime()));
            }
            if (!StringUtils.isEmpty(orderSearch.getSearchEndTime())) {
                query.setTimestamp("endTime", DateUtils.String2Date(orderSearch.getSearchEndTime()));
            }
        }
        List<Object[]> objects = query.list();
        List<ExportOrder> exportOrders = new ArrayList<>();
        for (Object[] object : objects) {
            ExportOrder exportOrder = new ExportOrder();
            exportOrder.setOrderCreateDate(DateUtils.formatDateTime((Date) object[0]));
            exportOrder.setOrderCode(object[1].toString());
            exportOrder.setOrderState(object[2].toString());
            exportOrder.setMerchantName(object[3].toString());
            exportOrder.setMerchantTell(object[4].toString());
            exportOrder.setMerchantMobileNo(object[5].toString());
            exportOrder.setUserphone(object[6].toString());
            exportOrder.setReceiverName(object[7].toString());
            exportOrder.setReceiverPhone(object[8].toString());
            exportOrder.setReceiverAdd(object[9].toString());
            exportOrder.setGoodsName(object[10].toString());
            exportOrder.setGoodsSkuValue(object[11] == null ? "" : object[11].toString());
            exportOrder.setGoodsCount(object[12].toString());
            exportOrders.add(exportOrder);
        }
        return exportOrders;
    }
}
