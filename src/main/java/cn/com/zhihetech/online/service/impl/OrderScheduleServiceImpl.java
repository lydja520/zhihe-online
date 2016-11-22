package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.online.service.IOrderScheduleService;
import cn.com.zhihetech.online.util.RealAmountUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.print.attribute.standard.MediaSize;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ydc on 16-6-30.
 */
@Service(value = "orderScheduleService")
public class OrderScheduleServiceImpl implements IOrderScheduleService {

    /**
     * pingpp 管理平台对应的 API key
     */
    private static String apiKey = AppConfig.PingppConfig.PINGPP_API_KEY;

    /**
     * pingpp 管理平台对应的应用 ID
     */
    private static String appId = AppConfig.PingppConfig.PINGPP_API_ID;

    @Resource(name = "orderDetailDao")
    private IOrderDetailDao orderDetailDao;
    @Resource(name = "orderDao")
    private IOrderDao orderDao;
    @Resource(name = "activityGoodsDao")
    private IActivityGoodsDao activityGoodsDao;
    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;
    @Resource(name = "merchantBillDetailDao")
    private IMerchantBillDetailDao merchantBillDetailDao;
    @Resource(name = "merchantScoreDao")
    private IMerchantScoreDao merchantScoreDao;
    @Resource(name = "goodsScoreDao")
    private IGoodsScoreDao goodsScoreDao;
    @Resource(name = "skuDao")
    private ISkuDao skuDao;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;

    /**
     * 活动商品订单 3分钟不支付，执行取消订单操作
     *
     * @param orderId
     */
    @Override
    public void executeAutoCancelActivityOrder(String orderId) {
        Order order = this.orderDao.findEntityById(orderId);
        if (order == null) {
            throw new SystemException("系统错误，请与管理员联系！");
        }
        if (order.getOrderState() == Constant.ORDER_STATE_NO_PAYMENT) {
            ActivityGoods activityGoods = order.getActivityGoods();
            IQueryParams queryParams = new GeneralQueryParams();
            queryParams.andEqual("orderId", orderId);
            order.setOrderState(Constant.ORDER_STATE_ALREADY_CANCEL);
            order.setDeleteState(true);
            this.orderDao.updateEntity(order);
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("order.orderId", orderId)
                    .andEqual("skuId", activityGoods.getSku().getSkuId());
            List<Object> oGoodsCounts = this.orderDetailDao.getProperty("goodsCount", null, queryParams);
            long goodsCount = (long) oGoodsCounts.get(0);
            if (activityGoods.getActivity().getCurrentState() == Constant.ACTIVITY_STATE_STARTED) {//如果活动还未结束，增加一个活动商品的剩余量
                this.activityGoodsDao.executeAddCount(activityGoods.getAgId(), 1);
            } else if (activityGoods.getActivity().getCurrentState() == Constant.ACTIVITY_STATE_FNISHED) {//如果活动已经结束,减少一个活动商品SKU的销量
                String skuId = activityGoods.getSku().getSkuId();
                this.skuDao.subSkuVolumeBySkuAndCount(skuId, goodsCount);
                this.goodsService.executeUpdateGoodsTransientInfo(activityGoods.getGoods().getGoodsId());
            }
        }
    }

    /**
     * 普通商品订单 超时不支付,执行取消订单操作
     *
     * @param orderId
     */
    @Override
    public void executeCancelOrder(String orderId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("order.orderId", orderId)
                .andEqual("order.orderState", Constant.ORDER_STATE_NO_PAYMENT);
        String[] params = new String[3];
        params[0] = "skuId";
        params[1] = "goods.goodsId";
        params[2] = "goodsCount";
        List<Object[]> orderDetails = this.orderDetailDao.getProperties(params, null, queryParams);
        if (orderDetails == null || orderDetails.isEmpty()) {
            return;
        }
        for (Object[] orderDetail : orderDetails) {
            String skuId = (String) orderDetail[0];
            String goodsId = (String) orderDetail[1];
            long goodsCount = (long) orderDetail[2];
            this.skuDao.subSkuVolumeBySkuAndCount(skuId, goodsCount);
            this.goodsService.executeUpdateGoodsTransientInfo(goodsId);
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("orderId", orderId);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("orderState", Constant.ORDER_STATE_ALREADY_CANCEL);
        this.orderDao.executeUpdate(paramAndValue, queryParams);
    }

    /**
     * 买家超时不确认收货 获取买家的账单状态和手机号码，进行自动确认收货短信提醒
     *
     * @param orderId
     * @return
     */
    @Override
    public Map<String, Object> getOrderState(String orderId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("orderId", orderId);
        String[] params = {"orderState", "user.userPhone", "orderCode"};
        List<Object[]> objects = this.orderDao.getProperties(params, null, queryParams);
        int orderState = (int) objects.get(0)[0];
        String phoneNum = (String) objects.get(0)[1];
        String orderCode = (String) objects.get(0)[2];
        Map<String, Object> orderStateAndPhoneNum = new HashMap<>();
        orderStateAndPhoneNum.put("orderState", orderState);
        orderStateAndPhoneNum.put("phoneNum", phoneNum);
        orderStateAndPhoneNum.put("orderCode", orderCode);
        return orderStateAndPhoneNum;
    }

    /**
     * 超时订单自动好评
     *
     * @param orderId
     */
    @Override
    public void executeEvaluateOrder(String orderId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("orderId", orderId);
        String[] selectors = new String[]{"orderState", "merchant.merchantId", "user.userId"};
        List<Object[]> oOrders = this.orderDao.getProperties(selectors, null, queryParams);
        if (oOrders.size() > 0) {
            int orderState = (int) oOrders.get(0)[0];
            if (orderState == Constant.ORDER_STATE_ALREADY_DELIVER) {
                String merchantId = (String) oOrders.get(0)[1];
                String userId = (String) oOrders.get(0)[2];
                Merchant merchant = new Merchant(merchantId);
                User user = new User(userId);
                Order order = new Order();
                order.setOrderId(orderId);

                MerchantScore merchantScore = new MerchantScore();
                merchantScore.setMerchant(merchant);
                merchantScore.setScore(5);
                this.merchantScoreDao.saveEntity(merchantScore);
                Date _now = new Date();
                queryParams = new GeneralQueryParams();
                queryParams.andEqual("order.orderId", orderId);
                List<Object> objects = this.orderDetailDao.getProperty("goods.goodsId", null, queryParams);
                for (Object o : objects) {
                    String goodsId = (String) o;
                    Goods goods = new Goods();
                    goods.setGoodsId(goodsId);
                    GoodsScore goodsScore = new GoodsScore();
                    goodsScore.setGoods(goods);
                    goodsScore.setUser(user);
                    goodsScore.setCreateDate(_now);
                    goodsScore.setOrder(order);
                    goodsScore.setEvaluate("默认好评！");
                    this.goodsScoreDao.saveEntity(goodsScore);
                }
                Map<String, Object> paramAndValue = new HashMap<>();
                paramAndValue.put("orderState", Constant.ORDER_STATE_ALREADY_EVALUATE);
                queryParams = new GeneralQueryParams();
                queryParams.andEqual("orderId", orderId);
                this.orderDao.executeUpdate(paramAndValue, queryParams);
            }
        }
    }

    /**
     * 买家超时不确认收货系统自动确认收货并添加账单详细
     *
     * @param orderId
     */
    @Override
    public void executeconfirmReceiverOrder(String orderId) {
        Order order = this.orderDao.findEntityById(orderId);
        Date now = new Date();
        if (order == null) {
            throw new SystemException("不存在该订单！");
        }
        if (order.getOrderState() == Constant.ORDER_STATE_ALREADY_DISPATCHER) {
            order.setOrderState(Constant.ORDER_STATE_ALREADY_DELIVER);
            order.setDeliverDate(now);
            MerchantBillDetail merchantBillDetail = new MerchantBillDetail();
            merchantBillDetail.setAmount(new BigDecimal(order.getOrderTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            merchantBillDetail.setPoundage(RealAmountUtils.calculatePoundage(order.getOrderTotal(), Constant.ORDER_HANDLER_POUNDAGE_RATE));
            merchantBillDetail.setRealAmount(RealAmountUtils.realAmount(order.getOrderTotal(), Constant.ORDER_HANDLER_POUNDAGE_RATE));
            merchantBillDetail.setOrder(order);
            merchantBillDetail.setCreateDate(now);
            merchantBillDetail.setHandleState(false);
            this.orderDao.updateEntity(order);
            this.merchantBillDetailDao.saveEntity(merchantBillDetail);
        }
    }

    /**
     * 系统自动确认退款操作
     *
     * @param orderId
     */
    @Override
    public void executeRefund(String orderId) {
        Order order = this.orderDao.findEntityById(orderId);
        if (order == null) {
            throw new SystemException("不存在该订单！");
        }
        if (order.getOrderState() == Constant.ORDER_STATE_WAIT_REFUND) {
            String chargeId = order.getPingPPId();
            Charge charge = null;
            Refund _refund = null;
            try {
                Pingpp.apiKey = this.apiKey;
                charge = Charge.retrieve(chargeId);
                Map<String, Object> refundMap = new HashMap();
                refundMap.put("amount", (int) (order.getOrderTotal() * 100));
                refundMap.put("description", "申请订单退款");
                Map<String, String> initialMetadata = new HashMap();
                initialMetadata.put("orderId", orderId);
                refundMap.put("metadata", initialMetadata);
                _refund = charge.getRefunds().create(refundMap);
            } catch (Exception e) {
                throw new SystemException("确认退款操作失败，请重试！", e);
            }
            if (_refund == null) {
                throw new SystemException("确认退款操作失败，请重试！");
            }
            if (order.getPayType().equals(AppConfig.PayType.ALI_PAY)) {
                int index = _refund.getFailureMsg().indexOf("http");
                String url = _refund.getFailureMsg().substring(index, _refund.getFailureMsg().length());
                order.setAlipayRefundUrl(url);
            }
            order.setOrderState(Constant.ORDER_STATE_REFUNDING);
            order.setConfirmRefundDate(new Date());
            this.orderDao.updateEntity(order);
        }
    }


    @Override
    public Order getById(String id) {
        return null;
    }

    @Override
    public void delete(Order order) {

    }

    @Override
    public Order add(Order order) {
        return null;
    }

    @Override
    public void update(Order order) {

    }

    @Override
    public List<Order> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<Order> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }
}
