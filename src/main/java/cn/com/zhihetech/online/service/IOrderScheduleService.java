package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.Map;

/**
 * Created by ydc on 16-6-30.
 */
public interface IOrderScheduleService extends SupportService<Order> {
    /**
     * 取消秒杀订单
     *
     * @param orderId
     */
    void executeAutoCancelActivityOrder(String orderId);

    /**
     * 取消普通订单
     *
     * @param orderId
     */
    void executeCancelOrder(String orderId);

    void executeconfirmReceiverOrder(String orderId);

    void executeRefund(String orderId);

    Map<String,Object> getOrderState(String orderId);

    void executeEvaluateOrder(String orderId);
}
