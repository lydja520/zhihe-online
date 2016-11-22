package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.WebChineseConfig;
import cn.com.zhihetech.online.components.ConfirmReceiveOrderSchedule;
import cn.com.zhihetech.online.components.RefundOrderSchedule;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.ICallbackService;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.PropertiesUtils;
import cn.com.zhihetech.online.util.SMSUtils;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Refund;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.print.attribute.standard.MediaSize;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.Calendar;

/**
 * Created by YangDaiChun on 2016/2/22.
 */
@Service(value = "callbackService")
public class CallbackServiceImpl implements ICallbackService {

    private final static Log log = LogFactory.getLog(CallbackServiceImpl.class);

    private final String PAY_SUCCESS = "charge.succeeded";  //付款成功回调
    private final String REFUND_SUCCESS = "refund.succeeded";  //退款成功回调

    @Resource(name = "orderDao")
    private IOrderDao orderDao;
    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;
    @Resource(name = "orderDetailDao")
    private IOrderDetailDao orderDetailDao;
    @Resource(name = "activityGoodsDao")
    private IActivityGoodsDao activityGoodsDao;
    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;
    @Resource(name = "skuDao")
    private ISkuDao skuDao;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;

    @Override
    public void updateOrderState(Event event) throws IOException {
        switch (event.getType()) {
            case PAY_SUCCESS:
                onPaySuccess(event);
                break;
            case REFUND_SUCCESS:
                onRefundSuccess(event);
                break;
            default:
                new SystemException("do not identify event type!");
        }
    }

    /**
     * 支付成功回调
     *
     * @param event
     */
    protected void onPaySuccess(Event event) {
        String orderNo = "order_no";
        Map<String, Object> eventData = event.getData();
        List<String> pingPPOrderNos = new LinkedList<String>();
        for (String key : eventData.keySet()) {
            LinkedTreeMap<String, String> eventDataNode = (LinkedTreeMap<String, String>) eventData.get(key);
            if (eventDataNode.containsKey(orderNo)) {
                pingPPOrderNos.add(eventDataNode.get(orderNo));
            }
        }
        for (String pingPPOrderNo : pingPPOrderNos) {
            IQueryParams queryParams = new GeneralQueryParams()
                    .andEqual("pingPPorderNo", pingPPOrderNo);
            List<Order> orders = this.orderDao.getEntities(queryParams);
            for (Order order : orders) {
                order.setOrderState(Constant.ORDER_STATE_NO_DISPATCHER);
                order.setPayDate(new Date());
                this.orderDao.updateEntity(order);
                String msgTxt = MessageFormat.format(WebChineseConfig.MsgTemplate.SEND_GOODS_REMIND,
                        " ", order.getOrderCode());
                notifyMerchantSendGoods(order.getMerchant().getContactMobileNO(), msgTxt);
            }
        }
    }


    /**
     * 通知订单对应的商家发货，另起一个线程发送短信不影响正常业务流程
     *
     * @param mobileNo 手机号码
     * @param msgTxt   短信内容
     */
    protected void notifyMerchantSendGoods(String mobileNo, String msgTxt) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SMSUtils.sendSMS(mobileNo, msgTxt);
                }
            }).start();
        } catch (Exception e) {
            log.error("发送短信提醒商家发货失败", e);
        }
    }


    /**
     * 退款回调
     *
     * @param event
     */
    protected void onRefundSuccess(Event event) {
        String tmp = JSON.toJSONString(event.getData().get("object"));
        Refund refund = JSONObject.parseObject(tmp, Refund.class);
        String orderId = refund.getMetadata().get("orderId");
        if (StringUtils.isEmpty(orderId)) {
            return;
        }
        IQueryParams queryParams = new GeneralQueryParams().andEqual("orderId", orderId);
        String payType = this.orderDao.getProperty("payType", null, queryParams).get(0).toString();
        if (payType.equals(AppConfig.PayType.WX_PAY)) {
            doWXPayRefund(orderId, refund);
        } else if (payType.equals(AppConfig.PayType.ALI_PAY)) {
            doAliPayRefund(orderId, refund);
        }
    }

    /**
     * 微信支付退款
     */
    protected void doWXPayRefund(String orderId, Refund refund) {
        boolean success = (refund.getSucceed() && refund.getStatus().equals("succeeded")) || (!refund.getSucceed() && refund.getStatus().equals("pending"));
        if (!success) {
            return;
        }
        IQueryParams queryParams = new GeneralQueryParams().andEqual("orderId", orderId)
                .andEqual("payType", AppConfig.PayType.WX_PAY)
                .andEqual("orderState", Constant.ORDER_STATE_REFUNDING);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("orderState", Constant.ORDER_STATE_ALREADY_REFUND);
        paramAndValue.put("refundOkDate", new Date());
        this.orderDao.executeUpdate(paramAndValue, queryParams);
        queryParams = new GeneralQueryParams()
                .andEqual("order.orderId", orderId);
        List<OrderDetail> orderDetails = this.orderDetailDao.getEntities(queryParams);
        for (OrderDetail orderDetail : orderDetails) {
            String skuId = orderDetail.getSkuId();
            this.skuDao.subSkuVolumeBySkuAndCount(skuId, orderDetail.getGoodsCount());
            this.goodsService.executeUpdateGoodsTransientInfo(orderDetail.getGoods().getGoodsId());
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("orderId", orderId);
        String[] selectors = new String[]{"user.userPhone", "orderCode"};
        List<Object[]> objects = this.orderDao.getProperties(selectors, null, queryParams);
        if (objects == null || objects.isEmpty()) {
            return;
        }
        String userPhone = (String) objects.get(0)[0];
        String orderCode = (String) objects.get(0)[1];
        String smsText = MessageFormat.format(WebChineseConfig.MsgTemplate.ORDER_REFUND_OK, orderCode);
        notifyUserRefundOk(userPhone, smsText);
    }

    /**
     * 通知用户实淘已经把钱退给了用户，另起一个线程发送短信不影响正常业务流程
     *
     * @param mobileNo 手机号码
     * @param msgTxt   短信内容
     */
    protected void notifyUserRefundOk(String mobileNo, String msgTxt) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SMSUtils.sendSMS(mobileNo, msgTxt);
                }
            }).start();
        } catch (Exception e) {
            log.error("发送'提醒退款'短信失败", e);
        }
    }

    /**
     * 支付宝退款处理
     */
    protected void doAliPayRefund(String orderId, Refund refund) {
        if (!refund.getStatus().equals("pending") || StringUtils.isEmpty(refund.getFailureMsg())) {
            return;
        }
        String url = refund.getFailureMsg();
        IQueryParams queryParams = new GeneralQueryParams().andEqual("orderId", orderId);
        Map<String, Object> values = new HashMap<>();
        values.put("alipayRefundUrl", url);
        this.orderDao.executeUpdate(values, queryParams);
    }
}
