package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.vo.ExportOrder;
import cn.com.zhihetech.online.vo.OrderAndOrderEvaluate;
import cn.com.zhihetech.online.vo.OrderEvaluateTemp;
import cn.com.zhihetech.online.vo.OrderSearch;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;
import com.pingplusplus.model.Charge;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/1/4.
 */
public interface IOrderService extends SupportService<Order> {

    ResponseMessage addOrderAndCharge(List<Order> orderStr, HttpServletRequest request);

    void executeUpdate(Map<String, Object> propertyAndValues, IQueryParams queryParams);

    void updateCarriage(String orderId, float carriage);

    Charge executeOrderPay(String orderId, HttpServletRequest request);

    ResponseMessage updateRefund(String orderId);

    /**
     * 退款
     *
     * @param orderId
     * @return
     */
    ResponseMessage executeConfirmRefund(String orderId);

    ResponseMessage deleteOrder(String orderId);

    void updateEvaluateMsg(OrderEvaluateTemp orderEvaluateTemp);

    Charge addActivityGoodsOrder(Order order, HttpServletRequest request);

    PageData<Order> getSearchPageData(HttpServletRequest request, IQueryParams queryParams, Pager pager, OrderSearch orderSearch);

    PageData<OrderAndOrderEvaluate> getOrderAndOrderEvaluate(String merchantId, Pager pager, IQueryParams queryParams);

    void updateOrderTotal(String orderId, float orderTotal);

    /**
     * 支付宝支付商家确认退款后，平台呢人工从支付宝退款成功后调用此方法将退款状态更新为已退款
     *
     * @param orderId
     */
    void executeAlipayRefunded(String orderId, String alipayRefundTransacCode);

    /**
     * 获取正在等待平台退款的订单(目前只有支付宝支付的订单退款才需由平台处理，微信支付可自动退款）
     *
     * @param pager
     * @param queryParams
     * @return
     */
    PageData<Order> getWaitPlatRefundOrders(Pager pager, IQueryParams queryParams);

    /**
     * 确认收货，添加账单详细
     *
     * @param orderId
     */
    void updateOrderAndAddBillDetail(String orderId);

    /**
     * 客服端支付成功之后回调此接口（防止用户重复支付）
     *
     * @param chargeOrderNO ping++中对应charge对象的order_no属性
     */
    void executeClientPaid(String chargeOrderNO);

    /**
     * 修改订单价格，并生成修改记录
     *
     * @param orderId
     * @param orderTotal
     */
    void updateAndAddEditRecord(String orderId, float orderTotal);

    /**
     * 获取指定的属性
     *
     * @param selector
     * @param pager
     * @param queryParams
     * @return
     */
    List<Object> getProperty(String selector, Pager pager, IQueryParams queryParams);

    List<ExportOrder> getExportOrders(String merchantId, OrderSearch orderSearch);

    void executeDispacher(String orderId, String carriageNum);

    void executeCancelOrder(String orderId);
}
