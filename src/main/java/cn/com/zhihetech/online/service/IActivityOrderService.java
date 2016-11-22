package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ActivityOrder;
import cn.com.zhihetech.online.bean.ActivityOrderDetail;
import cn.com.zhihetech.online.vo.ActivityOrderAndActivityOrderDeatils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ydc on 16-5-30.
 */
public interface IActivityOrderService extends SupportService<ActivityOrder> {
    ActivityOrderAndActivityOrderDeatils executeQueryOrder(String merchantId, String activityId);

    String executePay(String activityOrderId);

    void executePayResult(String out_trade_no, String trade_no, String seller_email, String buyer_email, String total_fee);

    void executePayResultConfirm(String out_trade_no, String trade_no, String seller_email, String buyer_email, String total_fee);

    ActivityOrderAndActivityOrderDeatils executeQueryOrderByOrderId(String activityOrderId);

    List<Object> getProperty(String selector, Pager pager, IQueryParams queryParams);
}
