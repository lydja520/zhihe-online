package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.commons.*;
import cn.com.zhihetech.online.components.*;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.online.service.IOrderService;
import cn.com.zhihetech.online.service.ISkuService;
import cn.com.zhihetech.online.util.*;
import cn.com.zhihetech.online.vo.ExportOrder;
import cn.com.zhihetech.online.vo.OrderAndOrderEvaluate;
import cn.com.zhihetech.online.vo.OrderEvaluateTemp;
import cn.com.zhihetech.online.vo.OrderSearch;
import cn.com.zhihetech.util.common.GeneratedNRandom;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.*;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.stereotype.Service;
import com.pingplusplus.Pingpp;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.Calendar;

/**
 * Created by YangDaiChun on 2016/1/4.
 */
@Service(value = "orderService")
public class OrderServiceImpl implements IOrderService {

    @Resource(name = "orderDao")
    private IOrderDao orderDao;
    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;
    @Resource(name = "orderDetailDao")
    private IOrderDetailDao orderDetailDao;
    @Resource(name = "merchantScoreDao")
    private IMerchantScoreDao merchantScoreDao;
    @Resource(name = "goodsScoreDao")
    private IGoodsScoreDao goodsScoreDao;
    @Resource(name = "activityGoodsDao")
    private IActivityGoodsDao activityGoodsDao;
    @Resource(name = "orderRecordDao")
    private IOrderEditRecordDao orderEditRecordDao;

    @Resource(name = "schedulerFactory")
    private Scheduler scheduler;
    
    @Resource(name = "merchantBillDetailDao")
    private IMerchantBillDetailDao merchantBillDetailDao;
    @Resource(name = "skuService")
    private ISkuService skuService;
    @Resource(name = "skuDao")
    private ISkuDao skuDao;
    @Resource(name = "goodsAttributeDao")
    private IGoodsAttributeDao goodsAttributeDao;


    /**
     * pingpp 管理平台对应的 API key
     */
    private static String apiKey = AppConfig.PingppConfig.PINGPP_API_KEY;

    /**
     * pingpp 管理平台对应的应用 ID
     */
    private static String appId = AppConfig.PingppConfig.PINGPP_API_ID;

    @Override
    public Order getById(String id) {
        return this.orderDao.findEntityById(id);
    }

    @Override
    public void delete(Order order) {

    }

    @Override
    public Order add(Order order) {
        return this.orderDao.saveEntity(order);
    }

    @Override
    public void update(Order order) {
        this.orderDao.updateEntity(order);
    }

    @Override
    public List<Order> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<Order> getPageData(Pager pager, IQueryParams queryParams) {
        return this.orderDao.getPageData(pager, queryParams);
    }

    /**
     * 添加普通订单并获取charge支付对象
     *
     * @param orders
     * @param request
     * @return
     */
    @Override
    public ResponseMessage addOrderAndCharge(List<Order> orders, HttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();
        long amount = 0;
        long currentTime = System.currentTimeMillis();
        String pingPPorderNo = Constant.BEGIN_CODE_PINGPP_ORDER + GeneratedNRandom.generated(2) + currentTime;

        List<String> goodsIds = new ArrayList<>();  //购买商品的ID
        List<Long> goodsCounts = new ArrayList<>(); //购买商品的数量

        Map<String, Long> skuIdsAndCount = new HashMap<>();

        for (Order order : orders) {//此for循环生成订单
            String orderBody = "";
            amount = (long) (order.getOrderTotal() * 100 + amount);
            currentTime = System.currentTimeMillis();
            order.setOrderCode(Constant.BEGIN_CODE_ORDER + GeneratedNRandom.generated(3) + currentTime);
            order.setCreateDate(new Date());
            order.setOrderState(Constant.ORDER_STATE_NO_PAYMENT);
            order.setPayDate(null);
            order.setPingPPorderNo(pingPPorderNo);
            order.setDeleteState(false);
            order.setOrderTotal(NumberUtils.floatScale(2, order.getOrderTotal()));

            String[] orderDetailsInfos = order.getOrderDetailInfo().split("\\&");
            List<OrderDetail> orderDetails = new LinkedList<>();
            float orderTotal = 0;
            List<Float> carriages = new LinkedList<>();
            for (String _orderDetailInfo : orderDetailsInfos) {
                String goodsId = _orderDetailInfo.substring(0, _orderDetailInfo.indexOf("*"));
                String goodsCount = _orderDetailInfo.substring(_orderDetailInfo.indexOf("*") + 1, _orderDetailInfo.indexOf("#"));
                String goodsPrice = _orderDetailInfo.substring(_orderDetailInfo.indexOf("#") + 1, _orderDetailInfo.length());

                goodsIds.add(goodsId);
                goodsCounts.add(Long.parseLong(goodsCount));

                Goods _goods = this.goodsDao.findEntityById(goodsId);
                if (_goods == null) {
                    throw new SystemException("提交订单失败，不存在该商品！");
                }
                if (!_goods.isOnsale()) {
                    throw new SystemException("提交订单失败，\"" + _goods.getGoodsName() + "\"已下架！");
                }
                if (_goods.getDeleteState()) {
                    throw new SystemException("提交订单失败，\"" + _goods.getGoodsName() + "\"已经被商家删除！");
                }

                IQueryParams queryParams = new GeneralQueryParams();
                queryParams.andEqual("goodsId", goodsId);
                List<Sku> oSkus = this.skuDao.getEntities(queryParams);
                if (oSkus == null || oSkus.isEmpty()) {
                    throw new SystemException("系统异常！该订单未添加");
                }

                Sku sku = oSkus.get(0);
                queryParams = new GeneralQueryParams();
                queryParams.andEqual("sku.skuId", sku.getSkuId());
                String skuName = this.skuService.getSkuValueBySkuId(sku.getSkuId());
                if (sku.getCurrentStock() < Integer.parseInt(goodsCount)) {
                    responseMessage.setCode(ResponseStatusCode.NOT_ENOUGH_STOCK);
                    responseMessage.setMsg("订单提交失败，" + _goods.getGoodsName() + " 库存量不足！");
                    responseMessage.setAttribute(_goods.getGoodsName());
                    responseMessage.setSuccess(false);
                    return responseMessage;
                }
                orderBody += _goods.getGoodsName() + " , "; //订单名称为订单中所有商品名

                skuIdsAndCount.put(sku.getSkuId(), Long.parseLong(goodsCount));

                OrderDetail _orderDetail = new OrderDetail();
                Goods goods = new Goods();
                goods.setGoodsId(goodsId);
                _orderDetail.setGoods(goods);
                _orderDetail.setGoodsCount(Long.parseLong(goodsCount));
                _orderDetail.setPrice(Float.parseFloat(goodsPrice));
                _orderDetail.setSkuId(sku.getSkuId());
                _orderDetail.setSkuName(skuName);
                orderDetails.add(_orderDetail);
                carriages.add(_goods.getCarriage());
                orderTotal = orderTotal + ((float) sku.getPrice() * Long.parseLong(goodsCount));
            }

            float maxCarriage = Collections.max(carriages);
            orderTotal = NumberUtils.floatScale(2, orderTotal + maxCarriage);
            if (orderTotal != order.getOrderTotal()) {
                responseMessage.setCode(ResponseStatusCode.NOT_ENOUGH_STOCK);
                responseMessage.setMsg("由于系统进行大幅升级，目前版本app不支持购买，请升级到最新版本！对你造成的不便，实淘深感抱歉！");
                responseMessage.setAttribute(order.getOrderName());
                responseMessage.setSuccess(false);
                return responseMessage;
            }

            OrderDetail firstDetail = orderDetails.get(0);
            Goods goods = this.goodsDao.findEntityById(firstDetail.getGoods().getGoodsId());
            Merchant merchant = goods.getMerchant();
            ImgInfo coverImg = goods.getCoverImg();
            order.setCoverImg(coverImg);
            order.setMerchant(merchant);

            order.setOrderName(orderBody.length() > 200 ? orderBody.substring(0, 200) : orderBody);
            order.setOriginalOrderTotal(order.getOrderTotal());
            Order _order = this.orderDao.saveEntity(order);
            Iterator<OrderDetail> it = orderDetails.iterator();
            while (it.hasNext()) {
                OrderDetail orderDetail = it.next();
                orderDetail.setOrder(_order);
                this.orderDetailDao.saveEntity(orderDetail);
            }
        }

        ChargeInfo chargeInfo = new ChargeInfo();
        chargeInfo.setChannel(orders.get(0).getPayType());//支付方式 "wx"或"alipay"
        chargeInfo.setOrderNo(pingPPorderNo);//支付订单号
        chargeInfo.setClientIp(request.getRemoteAddr()); //支付方IP地址
        chargeInfo.setAmount(amount);//支付的金额，以分为单位
        String orderName = orders.get(0).getOrderName();
        chargeInfo.setSubject(orderName.length() > 32 ? orderName.substring(0, 32) : orderName);  //商品名
        chargeInfo.setBody(orderName.length() > 100 ? orderName.substring(0, 100) : orderName);  //商品介绍

        Charge charge = PingppUtils.getCharge(chargeInfo);  //获取charge支付对象

        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("pingPPorderNo", pingPPorderNo);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("pingPPId", charge.getId());
        this.orderDao.executeUpdate(paramAndValue, queryParams);

        this.skuService.executeAddSkuVolumeBySkuAndCount(skuIdsAndCount);
        for (String goodsId : goodsIds) {
            this.goodsService.executeUpdateGoodsTransientInfo(goodsId);
        }

        List<String> orderIds = new ArrayList<>();
        for (Order order : orders) {
            orderIds.add(order.getOrderId());
        }
        cancelOrderScheduleJob(orderIds);

        responseMessage.setCode(ResponseStatusCode.SUCCESS_CODE);
        responseMessage.setMsg("添加订单成功");
        responseMessage.setAttribute(charge);
        responseMessage.setSuccess(true);
        return responseMessage;
    }

    /**
     * 添加活动(秒杀）商品订单（购买活动商品）
     *
     * @param order
     * @param request
     * @return
     */
    @Override
    public Charge addActivityGoodsOrder(Order order, HttpServletRequest request) {
        Charge charge = null;
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("user", order.getUser())
                .andEqual("activityGoods", order.getActivityGoods())
                .andEqual("deleteState", false);
        List<Object> orderIds = this.orderDao.getProperty("orderId", null, queryParams);
        if (orderIds.size() > 0) {
            throw new SystemException("一个用户在一次活动中同一件秒杀商品只能够购买一件，你已经抢购过此秒杀商品；如还未支付，请到我的订单中支付。！");
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("agId", order.getActivityGoods().getAgId());
        String[] params = new String[9];
        params[0] = "activityPrice";
        params[1] = "activity.beginDate";
        params[2] = "activity.endDate";
        params[3] = "goods.goodsId";
        params[4] = "goods.goodsName";
        params[5] = "goods.carriage";
        params[6] = "sku.skuId";
        params[7] = "sku.coverImg.imgInfoId";
        params[8] = "goods.coverImg.imgInfoId";

        List<Object[]> queryResult = this.activityGoodsDao.getProperties(params, null, queryParams);
        if (queryResult.size() <= 0) {
            throw new SystemException("找不到对应的活动商品");
        }

        //TODO  订单名称默认为商品名称
        order.setOrderName(String.valueOf(queryResult.get(0)[4]));
        order.setOrderTotal(NumberUtils.floatScale(2, order.getOrderTotal()));

        float activityPrice = (float) queryResult.get(0)[0];  //活动商品价格
        Date activityStartDate = (Date) queryResult.get(0)[1]; //活动开始时间
        Date activityEndDate = (Date) queryResult.get(0)[2];  //活动结束时间
        String goodsId = (String) queryResult.get(0)[3];  //活动商品对应的普通商品
        float carriage = Float.parseFloat(queryResult.get(0)[5] + "");
        String skuId = (String) queryResult.get(0)[6];
        String skuCoverImgId = (String) queryResult.get(0)[7];
        String goodsCoverImgId = (String) queryResult.get(0)[8];
        activityPrice = new BigDecimal(activityPrice).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        Date now = new Date();
        if (!(now.after(activityStartDate) && now.before(activityEndDate))) {
            throw new SystemException("活动还未开始，无法购买秒杀商品！");
        }

        /*==========获取charge支付对象========*/
        long currentTime = System.currentTimeMillis();
        String pingPPorderNo = Constant.BEGIN_CODE_PINGPP_ORDER + GeneratedNRandom.generated(2) + currentTime;
        String orderCode = Constant.BEGIN_CODE_ORDER + GeneratedNRandom.generated(3) + currentTime;
        float orderTotal = NumberUtils.floatScale(2, activityPrice + carriage);

        if (orderTotal != order.getOrderTotal()) {
            float _orderTotal = order.getOrderTotal() + carriage;
            _orderTotal = new BigDecimal(_orderTotal).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            if (orderTotal == _orderTotal) {
                order.setOrderTotal(_orderTotal);
            } else {
                throw new SystemException("订单提交失败，订单价格不对!");
            }
        }

        ChargeInfo chargeInfo = new ChargeInfo();
        chargeInfo.setAmount((long) (order.getOrderTotal() * 100));
        chargeInfo.setOrderNo(pingPPorderNo);
        chargeInfo.setChannel(order.getPayType());
        chargeInfo.setClientIp(request.getRemoteAddr());
        String subject = order.getOrderName();
        if (!StringUtils.isEmpty(subject)) {
            subject = subject.length() > 32 ? subject.substring(0, 32) : subject;
        } else {
            subject = "实淘订单";
        }
        chargeInfo.setSubject(subject);
        chargeInfo.setBody(subject);
        charge = PingppUtils.getCharge(chargeInfo);
         /*==========获取charge支付对象========*/

          /*========添加活动订单========*/
        Goods goods = this.goodsDao.findEntityById(goodsId);
        order.setOrderState(Constant.ORDER_STATE_NO_PAYMENT);

        order.setCoverImg(new ImgInfo(skuCoverImgId != null ? skuCoverImgId : goodsCoverImgId));
        order.setDeleteState(false);
        order.setCreateDate(new Date());
        order.setOrderCode(orderCode);
        order.setPingPPorderNo(pingPPorderNo);
        order.setPingPPId(charge.getId());
        order.setMerchant(goods.getMerchant());
        order.setOriginalOrderTotal(order.getOrderTotal());
        /*========添加活动订单========*/

          /*========添加活动订单详情========*/
        this.orderDao.saveEntity(order);
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setPrice(activityPrice);
        orderDetail.setGoods(goods);
        orderDetail.setGoodsCount(1);
        orderDetail.setSkuId(skuId);
        orderDetail.setSkuName(this.skuService.getSkuValueBySkuId(skuId));
        this.orderDetailDao.saveEntity(orderDetail);
        /*========添加活动订单详情========*/

        /*========更新活动商品剩余量========*/
        int agCount = 1;
        int count = this.activityGoodsDao.executeSubCount(order.getActivityGoods().getAgId(), agCount);
        if (count < 1) {
            throw new SystemException("亲，你来晚了哦，秒杀已经被抢光了！");
        }
        this.cancelActivityOrderScheduleJob(order.getOrderId());
        return charge;
         /*========更新活动商品剩余量========*/
    }

    /**
     * 增加取消秒杀商品定时任务
     * 提交订单超过指定时间未完成支付取消秒杀商品订单
     */
    private void cancelActivityOrderScheduleJob(String orderId) {
        Calendar nowTime = Calendar.getInstance();
        int cancelActivityOrderOffset = 3;  //默认3分钟未完成支付的的订单将自动取消
        try {
            cancelActivityOrderOffset = Integer.parseInt(PropertiesUtils.getProperties().getProperty("CANCEL_ACTIVITY_ORDER_OFFSET"));
        } catch (Exception e) {
        }
        if (cancelActivityOrderOffset == 0) {
            return;
        }
        nowTime.add(Calendar.MINUTE, cancelActivityOrderOffset);
        JobDetail jobDetail = JobBuilder.newJob(CancelActivityOrderSchedule.class).withIdentity(orderId, "cancelActivityOrderGroup").build();
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(orderId, "cancelActivityOrderGroup")
                .startAt(nowTime.getTime()).forJob(jobDetail).build();
        jobDetail.getJobDataMap().put("orderId", orderId);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new SystemException("活动订单自动取消定时任务创建失败", e);
        }
    }

    /**
     * 增加取消普通商品定时任务
     * 在指定时间为完成订单支付自动取消订单
     *
     * @param orderIds
     */
    private void cancelOrderScheduleJob(List<String> orderIds) {
        Calendar nowTime = Calendar.getInstance();
        int cancelActivityOrderOffset = 60 * 48;//默认48小时未完成支付的普通订单将自动取消
        try {
            cancelActivityOrderOffset = Integer.parseInt(PropertiesUtils.getProperties().getProperty("CANCEL_ORDER_OFFSET"));
        } catch (Exception ex) {
        }
        if (cancelActivityOrderOffset == 0) {
            return;
        }
        nowTime.add(Calendar.MINUTE, cancelActivityOrderOffset);
        for (String orderId : orderIds) {
            JobDetail jobDetail = JobBuilder.newJob(CancelOrderSchedule.class).withIdentity(orderId, "cancelOrderGroup").build();
            SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                    .withIdentity(orderId, "cancelOrderGroup")
                    .startAt(nowTime.getTime()).forJob(jobDetail).build();
            jobDetail.getJobDataMap().put("orderId", orderId);
            try {
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                throw new SystemException("普通商品订单自动取消定时任务创建失败", e);
            }
        }
    }

    /**
     * 支付订单（用户提交订单获取成功后未付款，重新付款）
     *
     * @param orderId
     * @param request
     * @return
     */
    @Override
    public Charge executeOrderPay(String orderId, HttpServletRequest request) {
        Order order = this.orderDao.findEntityById(orderId);
        if (order.getOrderState() != Constant.ORDER_STATE_NO_PAYMENT) {
            throw new SystemException("此订单当前状态不支持支付，请检查后重试！");
        }

        IQueryParams queryParams = new GeneralQueryParams();

        ActivityGoods activityGoods = order.getActivityGoods();
        if (activityGoods != null && StringUtils.isEmpty(activityGoods.getAgId())) {//条件成立，则为活动商品
            if (activityGoods.getAgCount() <= 0) {
                throw new SystemException("对不起，该活动商品已经抢光了！");
            }
        } else { //条件不成立，则为普通商品
            /**
             * 判断订单中的商品库存是否足够，是否已删除
             */
            queryParams.andEqual("order.orderId", orderId);
            List<OrderDetail> orderDetails = this.orderDetailDao.getEntities(null, queryParams);
            if (orderDetails == null || orderDetails.isEmpty()) {
                throw new SystemException("此订单为空，不能支付！");
            }
            for (OrderDetail orderDetail : orderDetails) {
                if (!orderDetail.getGoods().isOnsale() || orderDetail.getGoods().getDeleteState()) {
                    throw new SystemException("订单中的:" + orderDetail.getGoods().getGoodsName() + "已下架或已被删除！");
                }
            }
        }

        Charge charge = null;
        long currentTime = System.currentTimeMillis();
        String pingPPorderNo = Constant.BEGIN_CODE_PINGPP_ORDER + GeneratedNRandom.generated(2) + currentTime;
        if (!StringUtils.isEmpty(order.getOrderId())) {
            Pingpp.apiKey = this.apiKey;
            Map<String, Object> chargeMap = new HashMap<String, Object>();
            chargeMap.put("amount", (long) (order.getOrderTotal() * 100));
            chargeMap.put("currency", "cny");
            String orderName = order.getOrderName();
            chargeMap.put("subject", orderName.length() > 32 ? orderName.substring(0, 32) : orderName);
            chargeMap.put("body", orderName.length() > 100 ? orderName.substring(0, 100) : orderName);
            chargeMap.put("order_no", pingPPorderNo);
            chargeMap.put("channel", order.getPayType());
            chargeMap.put("client_ip", request.getRemoteAddr());
            Map<String, String> app = new HashMap<String, String>();
            app.put("id", this.appId);
            chargeMap.put("app", app);
            try {
                //发起交易请求
                charge = Charge.create(chargeMap);
            } catch (PingppException e) {
                throw new SystemException("调用支付控件失败，请重试！", e);
            }
            if (charge == null) {
                throw new SystemException("调用支付控件失败，请重试！");
            } else {
                if (charge.getPaid()) {
                    throw new SystemException("已经支付成功，不用重复支付");
                }
                if (charge.getRefunded()) {
                    throw new SystemException("该订单已经申请过退款，不支持支付操作");
                }
                queryParams = new GeneralQueryParams();
                queryParams.andEqual("orderId", orderId);
                Map<String, Object> paramAndValue = new HashMap<>();
                paramAndValue.put("pingPPorderNo", pingPPorderNo);
                paramAndValue.put("pingPPId", charge.getId());
                paramAndValue.put("payDate", new Date());
                this.orderDao.executeUpdate(paramAndValue, queryParams);
                return charge;
            }
        }
        return null;
    }

    /**
     * 买家手动取消订单
     *
     * @param orderId
     */
    @Override
    public void executeCancelOrder(String orderId) {
        Map<String, Object> paramAndValue = new HashMap<>();
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("order.orderId", orderId)
                .andEqual("order.orderState", Constant.ORDER_STATE_NO_PAYMENT);
        String[] params = new String[3];
        params[0] = "skuId";
        params[1] = "goods.goodsId";
        params[2] = "goodsCount";
        List<Object[]> orderDetails = this.orderDetailDao.getProperties(params, null, queryParams);
        if (orderDetails.size() <= 0) {
            return;
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("orderId", orderId);
        List<Object> activityGoodsIds = this.orderDao.getProperty("activityGoods.agId", null, queryParams);
        if (activityGoodsIds.size() <= 0) {
            throw new SystemException("对不起，系统找不到对应的订单！");
        }
        if (StringUtils.isEmpty((String) activityGoodsIds.get(0))) {  //如果该订单为普通商品订单，对订单中的商品进行减销量操作
            for (Object[] orderDetail : orderDetails) {
                String skuId = (String) orderDetail[0];
                String goodsId = (String) orderDetail[1];
                long goodsCount = (long) orderDetail[2];
                this.skuDao.subSkuVolumeBySkuAndCount(skuId, goodsCount);
                this.goodsService.executeUpdateGoodsTransientInfo(goodsId);
            }
        } else {  //如果该订单为活动订单，则提示无需手动取消
            throw new SystemException("取消操作失败！活动商品订单无需手动取消！提交订单未支付，3分钟后系统会自动取消该订单！");
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("orderId", orderId);
        paramAndValue.clear();
        paramAndValue.put("orderState", Constant.ORDER_STATE_ALREADY_CANCEL);
        this.orderDao.executeUpdate(paramAndValue, queryParams);
    }

    /**
     * 根据查询条件查出满足条件的订单
     *
     * @param request
     * @param queryParams
     * @param pager
     * @param orderSearch
     * @return
     */
    @Override
    public PageData<Order> getSearchPageData(HttpServletRequest request, IQueryParams queryParams, Pager pager, OrderSearch orderSearch) {
        StringBuffer startTime = null;
        StringBuffer endTime = null;
        queryParams.andEqual("deleteState", false);
        if (!StringUtils.isEmpty(orderSearch.getSearchCarriageNum())) {
            queryParams.andAllLike("carriageNum", orderSearch.getSearchCarriageNum());
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchOrderCode())) {
            queryParams.andAllLike("orderCode", orderSearch.getSearchOrderCode());
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchOrderState())) {
            String orderState = orderSearch.getSearchOrderState();
            int _orderState = Integer.parseInt(orderState);
            if (_orderState != 102 && _orderState != 101) {
                queryParams.andEqual("orderState", Integer.parseInt(orderState));
            } else if (_orderState == 102) {
                SubQueryParams subQueryParams = new SubQueryParams();
                subQueryParams.andEqual("orderState", Constant.ORDER_STATE_ALREADY_DELIVER)
                        .orEqual("orderState", Constant.ORDER_STATE_ALREADY_EVALUATE);
                queryParams.andSubParams(subQueryParams);
            } else if (_orderState == 101) {
                SubQueryParams subQueryParams = new SubQueryParams();
                subQueryParams.andEqual("orderState", Constant.ORDER_STATE_WAIT_REFUND)
                        .orEqual("orderState", Constant.ORDER_STATE_REFUNDING)
                        .orEqual("orderState", Constant.ORDER_STATE_ALREADY_REFUND);
                queryParams.andSubParams(subQueryParams);
            }
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchUserPhone())) {
            queryParams.andAllLike("user.userPhone", orderSearch.getSearchUserPhone());
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchMerchantName())) {
            queryParams.andAllLike("merchant.merchName", orderSearch.getSearchMerchantName());
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchStartTime()) && !StringUtils.isEmpty(orderSearch.getSearchEndTime())) {
            startTime = new StringBuffer(orderSearch.getSearchStartTime());
            endTime = new StringBuffer(orderSearch.getSearchEndTime());
            startTime.append(" 00:00:00");
            endTime.append(" 23:59:59");
            Date _startTime = DateUtils.String2DateTime(startTime.toString());
            Date _endTime = DateUtils.String2DateTime(endTime.toString());
            queryParams.andMoreAndEq("createDate", _startTime).andLessAndEq("createDate", _endTime);
        } else if (!StringUtils.isEmpty(orderSearch.getSearchStartTime())) {
            startTime = new StringBuffer(orderSearch.getSearchStartTime());
            startTime.append(" 00:00:00");
            Date _startTime = DateUtils.String2DateTime(startTime.toString());
            queryParams.andMoreAndEq("createDate", _startTime);
        } else if (!StringUtils.isEmpty(orderSearch.getSearchEndTime())) {
            endTime = new StringBuffer(orderSearch.getSearchEndTime());
            endTime.append(" 23:59:59");
            Date _endTime = DateUtils.String2DateTime(endTime.toString());
            queryParams.andLessAndEq("createDate", _endTime);
        }
        if (StringUtils.isEmpty((String) request.getParameter("sort"))) {
            queryParams.sort("createDate", cn.com.zhihetech.util.hibernate.Order.DESC);
        }
        PageData<Order> orderPageData = this.orderDao.getPageData(pager, queryParams);
        return orderPageData;
    }

    /**
     * 根据商家ID查询商品的评论
     *
     * @param merchantId
     * @param pager
     * @param queryParams
     * @return
     */
    @Override
    public PageData<OrderAndOrderEvaluate> getOrderAndOrderEvaluate(String merchantId, Pager pager, IQueryParams queryParams) {
        PageData<OrderAndOrderEvaluate> orderAndEvaluatePgData = new PageData<>();
        queryParams.andEqual("orderState", Constant.ORDER_STATE_ALREADY_EVALUATE).andEqual("merchant.merchantId", merchantId).sort("createDate", cn.com.zhihetech.util.hibernate.Order.DESC);
        PageData<Order> orderPageData = this.orderDao.getPageData(pager, queryParams);
        orderAndEvaluatePgData.setTotalPage(orderPageData.getTotalPage());
        orderAndEvaluatePgData.setTotal(orderPageData.getTotal());
        orderAndEvaluatePgData.setPageSize(orderPageData.getPageSize());
        orderAndEvaluatePgData.setPage(orderPageData.getPage());
        List<OrderAndOrderEvaluate> orderAndOrderEvaluates = new ArrayList<>();

        OrderAndOrderEvaluate orderAndOrderEvaluate = null;
        List<GoodsScore> goodsScores = null;
        for (Order order : orderPageData.getRows()) {
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("order.orderId", order.getOrderId());
            goodsScores = this.goodsScoreDao.getEntities(null, queryParams);
            orderAndOrderEvaluate = new OrderAndOrderEvaluate();
            orderAndOrderEvaluate.setOrder(order);
            orderAndOrderEvaluate.setGoodsScores(goodsScores);
            orderAndOrderEvaluates.add(orderAndOrderEvaluate);
        }
        orderAndEvaluatePgData.setRows(orderAndOrderEvaluates);
        return orderAndEvaluatePgData;
    }

    /**
     * 买家申请退款
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseMessage updateRefund(String orderId) {
        Order order = this.orderDao.findEntityById(orderId);
        ResponseMessage responseMessage = new ResponseMessage();
        if (order.getOrderState() != Constant.ORDER_STATE_NO_DISPATCHER) {
            responseMessage.setCode(ResponseStatusCode.SYSTEM_ERROR_CODE);
            responseMessage.setAttribute(null);
            responseMessage.setSuccess(true);
            responseMessage.setMsg("该状态下，不支持退款操作");
            return responseMessage;
        }
        Date now = new Date();
        order.setApplyRefundDate(now);
        order.setOrderState(Constant.ORDER_STATE_WAIT_REFUND);
        this.orderDao.updateEntity(order);
        String smsText = MessageFormat.format(WebChineseConfig.MsgTemplate.ORDER_REFUND_WITHDRAW, DateUtils.formatDateTime(now), order.getOrderCode());
        SMSUtils.sendSMS(order.getMerchant().getContactMobileNO(), smsText);
        this.confimRefundSchedule(orderId);
        responseMessage.setCode(ResponseStatusCode.SUCCESS_CODE);
        responseMessage.setAttribute(null);
        responseMessage.setSuccess(true);
        responseMessage.setMsg("申请提交成功，等待卖家退款");
        return responseMessage;
    }

    /**
     * 增加自动退款定时任务
     */
    private void confimRefundSchedule(String orderId) {
        Calendar nowTime = Calendar.getInstance();
        int refundOffet = 24 * 60 * 2;
        try {
            refundOffet = Integer.parseInt(PropertiesUtils.getProperties().getProperty("EXECUTE_REFUND_OFFSET"));
        } catch (Exception e) {
        }
        if (refundOffet == 0) {
            return;
        }
        nowTime.add(Calendar.MINUTE, refundOffet);
        JobDetail jobDetail = JobBuilder.newJob(RefundOrderSchedule.class).withIdentity(orderId, "refundOrderGroup").build();
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(orderId, "refundOrderGroup")
                .startAt(nowTime.getTime()).forJob(jobDetail).build();
        jobDetail.getJobDataMap().put("orderId", orderId);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new SystemException("订单确认退款定时任务创建失败！", e);
        }
    }

    /**
     * 删除订单操作
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseMessage deleteOrder(String orderId) {
        Order order = this.orderDao.findEntityById(orderId);
        ResponseMessage responseMessage = new ResponseMessage();
        if ((order.getOrderState() == Constant.ORDER_STATE_ALREADY_CANCEL) || (order.getOrderState() == Constant.ORDER_STATE_ALREADY_EVALUATE) || (order.getOrderState() == Constant.ORDER_STATE_ALREADY_REFUND)) {
            order.setDeleteState(true);
            this.orderDao.updateEntity(order);
            responseMessage.setCode(ResponseStatusCode.SUCCESS_CODE);
            responseMessage.setAttribute(null);
            responseMessage.setSuccess(true);
            responseMessage.setMsg("订单删除成功!");
            return responseMessage;
        }
        responseMessage.setCode(ResponseStatusCode.SYSTEM_ERROR_CODE);
        responseMessage.setAttribute(null);
        responseMessage.setSuccess(true);
        responseMessage.setMsg("该状态下，不支持删除订单操作!");
        return responseMessage;
    }

    /**
     * 订单评论
     *
     * @param orderEvaluateTemp
     */
    @Override
    public void updateEvaluateMsg(OrderEvaluateTemp orderEvaluateTemp) {
        Order order = this.orderDao.findEntityById(orderEvaluateTemp.getOrderId());
        if (order.getOrderState() != Constant.ORDER_STATE_ALREADY_DELIVER) {
            throw new SystemException("该订单当前状态下不支持评论操作，请刷新后在试！");
        }
        Merchant merchant = order.getMerchant();
        MerchantScore merchantScore = new MerchantScore();
        merchantScore.setMerchant(merchant);
        merchantScore.setScore(orderEvaluateTemp.getScore());
        this.merchantScoreDao.saveEntity(merchantScore);
        List<GoodsScore> goodsScores = orderEvaluateTemp.getGoodsScores();
        for (GoodsScore goodsScore : goodsScores) {
            Goods goods = new Goods();
            goods.setGoodsId(goodsScore.getGoodsId());
            goodsScore.setGoods(goods);
            goodsScore.setUser(order.getUser());
            goodsScore.setCreateDate(new Date());
            goodsScore.setOrder(order);
            this.goodsScoreDao.saveEntity(goodsScore);
        }
        order.setOrderState(Constant.ORDER_STATE_ALREADY_EVALUATE);
        order.setEvaluateDate(new Date());
        this.orderDao.updateEntity(order);
    }

    /**
     * 订单确认退款操作
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseMessage executeConfirmRefund(String orderId) {
        Order order = this.orderDao.findEntityById(orderId);
        ResponseMessage responseMessage = new ResponseMessage(200, true, "已确认退款，请等待退款结果！");
        if (order.getOrderState() != Constant.ORDER_STATE_WAIT_REFUND) {
            responseMessage.setCode(ResponseStatusCode.SYSTEM_ERROR_CODE);
            responseMessage.setMsg("该状态下，不支持退款操作");
            return responseMessage;
        }
        if (order.getOrderState() == Constant.ORDER_STATE_ALREADY_REFUND) {
            throw new SystemException("此订单已成功退款，无须重复确认！");
        }
        if (order.getOrderState() == Constant.ORDER_STATE_REFUNDING) {
            throw new SystemException("退款申请已确认,平台正在退款中，却勿重复操作！");
        }
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
        return responseMessage;
    }

    @Override
    public void executeUpdate(Map<String, Object> propertyAndValues, IQueryParams queryParams) {
        this.orderDao.executeUpdate(propertyAndValues, queryParams);
    }

    /**
     * 修改订单运费
     *
     * @param orderId
     * @param carriage
     */
    @Override
    public void updateCarriage(String orderId, float carriage) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("orderId", orderId);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("carriage", carriage);
        IQueryParams queryParams1 = new GeneralQueryParams();
        queryParams1.andEqual("order.orderId", orderId);
        List<OrderDetail> orderDetails = this.orderDetailDao.getEntities(null, queryParams1);
        float goodsTotalPrice = 0;
        for (OrderDetail orderDetail : orderDetails) {
            goodsTotalPrice += orderDetail.getGoodsCount() * orderDetail.getGoods().getPrice();
        }
        paramAndValue.put("orderTotal", goodsTotalPrice + carriage);
        this.orderDao.executeUpdate(paramAndValue, queryParams);
    }

    /**
     * 修改订单总价
     *
     * @param orderId
     * @param orderTotal
     */
    @Override
    public void updateOrderTotal(String orderId, float orderTotal) {
        return;
    }

    /**
     * 支付宝支付商家确认退款后，平台人工从支付宝退款成功后调用此方法将退款状态更新为已退款
     *
     * @param orderId
     */
    @Override
    public void executeAlipayRefunded(String orderId, String alipayRefundTransacCode) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("orderId", orderId)
                .andEqual("orderState", Constant.ORDER_STATE_REFUNDING)
                .andEqual("payType", AppConfig.PayType.ALI_PAY)
                .andNotNull("alipayRefundUrl");
        long total = this.orderDao.getRecordTotal(queryParams);
        if (total <= 0) {
            throw new SystemException("操作失败，不存在此订单或此当单当前状态不支持此操作，请刷新后重试！");
        }
        Map<String, Object> values = new HashMap<>();
        values.put("orderState", Constant.ORDER_STATE_ALREADY_REFUND);
        values.put("refundOkDate", new Date());
        values.put("alipayRefundTransacCode", alipayRefundTransacCode);
        this.orderDao.executeUpdate(values, queryParams);
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
            LogFactory.getLog(this.getClass()).error("发送'提醒退款'短信失败", e);
        }
    }

    /**
     * 获取正在等待平台退款的订单(目前只有支付宝支付的订单退款才需由平台处理，微信支付可自动退款）
     *
     * @param pager
     * @param queryParams
     * @return
     */
    @Override
    public PageData<Order> getWaitPlatRefundOrders(Pager pager, IQueryParams queryParams) {
        queryParams = queryParams == null ? new GeneralQueryParams() : queryParams;
        queryParams.andEqual("orderState", Constant.ORDER_STATE_REFUNDING)
                .andEqual("payType", AppConfig.PayType.ALI_PAY)
                .andNotNull("alipayRefundUrl");
        return this.orderDao.getPageData(pager, queryParams);
    }

    /**
     * 订单确认收货和增加订单账单
     *
     * @param orderId
     */
    @Override
    public void updateOrderAndAddBillDetail(String orderId) {
        Order order = this.orderDao.findEntityById(orderId);
        Date now = new Date();
        if (order == null) {
            throw new SystemException("不存在该订单！");
        }
        if (order.getOrderState() != Constant.ORDER_STATE_ALREADY_DISPATCHER) {
            throw new SystemException("当前状态订单不支持确认收货操作，请确认当前订单状态后在试！");
        }
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
/*        this.evaluateOrderScheduleJob(orderId);*/
    }


    /**
     * 增加超时自动评价定时任务
     *
     * @param orderId
     */
    private void evaluateOrderScheduleJob(String orderId) {
        Calendar nowTime = Calendar.getInstance();
        int evaluateOrderOffset = 60 * 24 * 5;//默认48小时未完成支付的普通订单将自动取消
        try {
            evaluateOrderOffset = Integer.parseInt(PropertiesUtils.getProperties().getProperty("EVALUATE_ORDER_OFFSET"));
        } catch (Exception ex) {
        }
        if (evaluateOrderOffset == 0) {
            return;
        }
        nowTime.add(Calendar.MINUTE, evaluateOrderOffset);

        JobDetail jobDetail = JobBuilder.newJob(EvaluateOrderSchedule.class).withIdentity(orderId, "evaluateOrderGroup").build();
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(orderId, "evaluateOrderGroup")
                .startAt(nowTime.getTime()).forJob(jobDetail).build();
        jobDetail.getJobDataMap().put("orderId", orderId);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new SystemException("商品订单超时评价定时任务创建失败", e);
        }
    }

    /**
     * 客服端支付成功之后回调此接口（防止用户重复支付）
     *
     * @param chargeOrderNO ping++中对应charge对象的order_no属性
     */
    @Override
    public void executeClientPaid(String chargeOrderNO) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("pingPPorderNo", chargeOrderNO)
                .andEqual("orderState", Constant.ORDER_STATE_NO_PAYMENT)
                .andEqual("deleteState", false);

        Map<String, Object> values = new HashMap<>();
        values.put("orderState", Constant.ORDER_STATE_PAYDING);

        this.orderDao.executeUpdate(values, queryParams);
    }

    /**
     * 修改订单总价，并保存修改记录
     *
     * @param orderId
     * @param orderTotal
     */
    @Override
    public void updateAndAddEditRecord(String orderId, float orderTotal) {
        Order order = this.orderDao.findEntityById(orderId);
        if (order == null) {
            throw new SystemException("不存在该订单");
        }
        float lastTotal = order.getOrderTotal();
        order.setOrderTotal(orderTotal);
        this.orderDao.updateEntity(order);
        OrderEditRecord orderEditRecord = new OrderEditRecord();
        orderEditRecord.setOrderState(order.getOrderState());
        orderEditRecord.setOriginalPrice(lastTotal);//获取上次修改的真实价格
        orderEditRecord.setNowPrice(order.getOrderTotal());
        orderEditRecord.setEditDate(new Date());
        orderEditRecord.setOrder(order);
        this.orderEditRecordDao.saveEntity(orderEditRecord);
    }

    /**
     * 获取指定的属性
     *
     * @param selector
     * @param pager
     * @param queryParams
     * @return
     */
    @Override
    public List<Object> getProperty(String selector, Pager pager, IQueryParams queryParams) {
        return this.orderDao.getProperty(selector, pager, queryParams);
    }

    @Override
    public List<ExportOrder> getExportOrders(String merchantId, OrderSearch orderSearch) {
        return this.orderDao.getExportOrderByMerchantId(merchantId, orderSearch);
    }

    /**
     * 执行发货
     *
     * @param orderId
     * @param carriageNum
     */
    @Override
    public void executeDispacher(String orderId, String carriageNum) {
        Order order = this.orderDao.findEntityById(orderId);
        if (order == null) {
            throw new SystemException("不存在该订单！");
        }
        if (order.getOrderState() != Constant.ORDER_STATE_NO_DISPATCHER) {
            throw new SystemException("该订单不支持发货操作，因为订单状态已经发生改变，请刷新页面后再试！");
        }
        order.setCarriageNum(carriageNum);
        order.setDispatcherDate(new Date());
        order.setOrderState(Constant.ORDER_STATE_ALREADY_DISPATCHER);
        this.orderDao.updateEntity(order);
        this.confirmReceive(orderId);
    }

    /**
     * 添加自动确认收货定时任务和定时确认收货短信提醒
     */
    private void confirmReceive(String orderId) {
        Calendar nowTime = Calendar.getInstance();
        Calendar nowTime2 = (Calendar) nowTime.clone();
        Calendar nowTime3 = (Calendar) nowTime.clone();

        int receiverOrderOffset = 7 * 24 * 60; //确认收货订单时间
        int receiverOrderMsgOffset = 2;  //确认收货订单提醒时间
        int evaluateOrderOffset = 60 * 24 * 5; //默认确认收货5天不评价会自动评价
        try {
            receiverOrderOffset = Integer.parseInt(PropertiesUtils.getProperties().getProperty("CONFIRM_RECEIVER_ORDER_OFFSET"));
            receiverOrderMsgOffset = Integer.parseInt(PropertiesUtils.getProperties().getProperty("CONFIRM_RECEIVER_ORDER_MSG_OFFSET"));
            evaluateOrderOffset = Integer.parseInt(PropertiesUtils.getProperties().getProperty("EVALUATE_ORDER_OFFSET"));
        } catch (Exception e) {
        }

        /*↓↓↓↓↓↓↓↓↓创建确认收货定时任务↓↓↓↓↓↓↓↓*/
        if (receiverOrderOffset == 0) {
            return;
        }
        nowTime.add(Calendar.MINUTE, receiverOrderOffset);
        JobDetail jobDetail = JobBuilder.newJob(ConfirmReceiveOrderSchedule.class).withIdentity(orderId, "receiverOrderGroup").build();
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(orderId, "receiverOrderGroup")
                .startAt(nowTime.getTime()).forJob(jobDetail).build();
        jobDetail.getJobDataMap().put("orderId", orderId);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new SystemException("订单确认收货定时任务创建失败！", e);
        }
        /*↑↑↑↑↑↑↑↑↑创建确认收货定时任务↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓创建确认收货短信提醒定时任务↓↓↓↓↓↓↓↓*/
        if (receiverOrderMsgOffset == 0) {
            return;
        }
        nowTime2.add(Calendar.MINUTE, receiverOrderMsgOffset);
        JobDetail jobDetail2 = JobBuilder.newJob(ConfirmReceiverOrderMsgSchedule.class).withIdentity(orderId, "receiverOrderMsgGroup").build();
        SimpleTrigger trigger2 = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(orderId, "receiverOrderMsgGroup")
                .startAt(nowTime2.getTime()).forJob(jobDetail2).build();
        jobDetail2.getJobDataMap().put("orderId", orderId);
        try {
            scheduler.scheduleJob(jobDetail2, trigger2);
        } catch (SchedulerException e) {
            throw new SystemException("订单确认收货短信提醒定时任务创建失败！", e);
        }
        /*↑↑↑↑↑↑↑↑↑创建确认收货短信提醒定时任务↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓创建订单评价定时任务↓↓↓↓↓↓↓↓*/
        if (evaluateOrderOffset == 0) {
            return;
        }
        nowTime3.add(Calendar.MINUTE, evaluateOrderOffset + receiverOrderOffset);
        JobDetail jobDetail3 = JobBuilder.newJob(EvaluateOrderSchedule.class).withIdentity(orderId, "evaluateOrderGroup").build();
        SimpleTrigger trigger3 = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(orderId, "evaluateOrderGroup")
                .startAt(nowTime3.getTime()).forJob(jobDetail3).build();
        jobDetail3.getJobDataMap().put("orderId", orderId);
        try {
            scheduler.scheduleJob(jobDetail3, trigger3);
        } catch (SchedulerException e) {
            throw new SystemException("商品订单超时评价定时任务创建失败", e);
        }
        /*↑↑↑↑↑↑↑↑↑创建订单评价定时任务↑↑↑↑↑↑↑*/

    }
}
