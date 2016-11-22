package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.components.CancelActivityOrderSchedule;
import cn.com.zhihetech.online.components.CancelOrderSchedule;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.StatusCodeException;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.online.util.NumberUtils;
import cn.com.zhihetech.online.util.PingppUtils;
import cn.com.zhihetech.online.util.PropertiesUtils;
import cn.com.zhihetech.online.vo.OrderAndOrderDetailInfo;
import cn.com.zhihetech.online.vo.OrderDetailInfo;
import cn.com.zhihetech.online.vo.OrderInfo;
import cn.com.zhihetech.util.common.GeneratedNRandom;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import com.pingplusplus.model.Charge;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.Calendar;

/**
 * Created by YangDaiChun on 2016/7/14.
 */
@Service("v2OrderService")
public class V2OrderServiceImpl implements IV2OrderService {

    @Resource(name = "skuDao")
    private ISkuDao skuDao;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;
    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;
    @Resource(name = "activityGoodsService")
    private IActivityGoodsService activityGoodsService;
    @Resource(name = "activityGoodsDao")
    private IActivityGoodsDao activityGoodsDao;
    @Resource(name = "orderDao")
    private IOrderDao orderDao;
    @Resource(name = "orderDetailDao")
    private IOrderDetailDao orderDetailDao;
    @Resource(name = "goodsAttributeService")
    private IGoodsAttributeService goodsAttributeService;
    @Resource(name = "schedulerFactory")
    private Scheduler scheduler;
    @Resource(name = "skuService")
    private ISkuService skuService;

    /**
     * 提交普通商品订单并获取charge对象
     *
     * @param orderInfos
     * @param clientIp
     * @return
     */
    @Override
    public Charge executeSubmitOrderAndGetCharge(String userId, List<OrderInfo> orderInfos, String clientIp) {
        Charge charge = null;
        OrderInfo[] orderInfoArray = new OrderInfo[orderInfos.size()];
        for (int i = 0; i < orderInfos.size(); i++) {
            orderInfoArray[i] = orderInfos.get(i);
        }

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓根据客户端传来的订单信息，调用ping++SDK生成charge支付对象↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        List<OrderAndOrderDetailInfo> orderAndOrderDetailInfos = this.getOrderAndOrderDetailInfoList(orderInfoArray);
        ChargeInfo chargeInfo = this.getChargeInfo(orderAndOrderDetailInfos, clientIp);
        charge = PingppUtils.getCharge(chargeInfo);
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑根据客户端传来的订单信息，调用ping++SDK生成charge支付对象↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        Map<String, Long> skuIdsAndCount = new HashMap<>();
        List<String> goodsIds = new ArrayList<>();
        List<String> orderIds = new ArrayList<>();

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓把订单和订单详情持久化到数据库↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        for (OrderAndOrderDetailInfo orderAndOrderDetailInfo : orderAndOrderDetailInfos) {
            Order order = orderAndOrderDetailInfo.getOrder();
            order.setPingPPId(charge.getId());
            order.setUser(new User(userId));
            this.orderDao.saveEntity(order);
            orderIds.add(order.getOrderId());
            for (OrderDetail orderDetail : orderAndOrderDetailInfo.getOrderDetails()) {
                String skuId = orderDetail.getSkuId();
                if (skuIdsAndCount.containsKey(skuId)) {
                    skuIdsAndCount.put(skuId, skuIdsAndCount.get(skuId) + orderDetail.getGoodsCount());
                } else {
                    skuIdsAndCount.put(skuId, orderDetail.getGoodsCount());
                }
                orderDetail.setOrder(order);
                this.orderDetailDao.saveEntity(orderDetail);
                goodsIds.add(orderDetail.getGoods().getGoodsId());
            }
        }
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑把订单和订单详情持久化到数据库↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓当用户下订单后增加sku销量和加商品的总销量↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        this.skuService.executeAddSkuVolumeBySkuAndCount(skuIdsAndCount);
        for (String goodsId : goodsIds) {
            this.goodsService.executeUpdateGoodsTransientInfo(goodsId);
        }
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑当用户下订单后增加sku销量和加商品的总销量↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        /*添加 取消订单 定时任务*/
        this.cancelOrderScheduleJob(orderIds);

        return charge;
    }

    /**
     * 添加活动商品订单并获取charge
     *
     * @return
     */
    public Charge executeSubmitSeckillOrderAndGetCharge(OrderInfo orderInfo, String clientIp) {
        Charge charge = null;

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓根据客户端传来的订单信息，调用ping++SDK生成charge支付对象↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        List<OrderAndOrderDetailInfo> orderAndOrderDetailInfos = this.getOrderAndOrderDetailInfoList(orderInfo);
        ChargeInfo chargeInfo = this.getChargeInfo(orderAndOrderDetailInfos, clientIp);
        charge = PingppUtils.getCharge(chargeInfo);
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑根据客户端传来的订单信息，调用ping++SDK生成charge支付对象↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        //判断根据客户端提交信息生成的秒杀商品订单及订单详情是否满足条件
        if (orderAndOrderDetailInfos.size() > 1) {
            throw new SystemException("对不起！秒杀商品订单暂不支持批量支付！");
        }
        if (orderAndOrderDetailInfos.get(0).getOrderDetails().size() > 1) {
            throw new SystemException("对不起！一条秒杀商品订单暂不支持购买多种商品！");
        }

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓把订单和订单详情持久化到数据库↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        Order order = orderAndOrderDetailInfos.get(0).getOrder();
        OrderDetail orderDetail = orderAndOrderDetailInfos.get(0).getOrderDetails().get(0);
        order.setPingPPId(charge.getId());
        order.setUser(new User(orderInfo.getUserId()));
        this.orderDao.saveEntity(order);
        orderDetail.setOrder(order);
        this.orderDetailDao.saveEntity(orderDetail);
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑把订单和订单详情持久化到数据库↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        //活动商品的剩余量减去买家购买的数量
        String agId = order.getActivityGoods().getAgId();
        int agCount = (int) orderDetail.getGoodsCount();
        this.executeSubActivityGoodsCounts(agId, agCount);

        //添加定时取消 秒杀商品订单 定时任务
        this.cancelActivityOrderScheduleJob(order.getOrderId());

        return charge;
    }

    /**
     * 用户提交订单后，取消支付，在订单管理里重新支付
     *
     * @param userId
     * @param orderId
     * @param remoteAddr
     * @return
     */
    @Override
    public Charge executeOrderAgainPay(String userId, String orderId, String remoteAddr) {
        Order order = this.orderDao.findEntityById(orderId);
        IQueryParams queryParams = new GeneralQueryParams();
        if (order.getOrderState() != Constant.ORDER_STATE_NO_PAYMENT) {
            throw new SystemException("该订单当前状态不支持支付，请检查后重试！");
        }
        ActivityGoods activityGoods = order.getActivityGoods();

         /*↓↓↓↓↓↓↓↓↓↓↓↓↓判断是否是普通商品订单，如果是，则判断普通商品是否下架和删除↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        if (activityGoods == null) {//是普通商品
            queryParams.andEqual("order.orderId", orderId);
            List<OrderDetail> orderDetails = this.orderDetailDao.getEntities(null, queryParams);
            for (OrderDetail orderDetail : orderDetails) {
                if (!orderDetail.getGoods().isOnsale()) {
                    throw new SystemException("订单中的:" + orderDetail.getGoods().getGoodsName() + "已被商家下架！");
                }
                if (orderDetail.getGoods().getDeleteState()) {
                    throw new SystemException("订单中的:" + orderDetail.getGoods().getGoodsName() + "已被商家删除！");
                }
            }
        }
         /*↑↑↑↑↑↑↑↑↑↑↑↑↑判断是否是普通商品订单，如果是，则判断普通商品是否下架和删除↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓获取charge支付对象，并重新生成新的订单号↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        ChargeInfo chargeInfo = this.getChargeInfo(order, remoteAddr);
        Charge charge = PingppUtils.getCharge(chargeInfo);
        order.setPingPPorderNo(chargeInfo.getOrderNo());
        order.setPingPPId(charge.getId());
        this.orderDao.updateEntity(order);
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑获取charge支付对象，并重新生成新的订单号↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        return charge;
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
     * 购买活动商品，判断是活动商品的剩余数量是否大于等于购买数量
     * 如果满足条件，则剩余数量减去购买数量
     *
     * @param agId
     * @param agCount
     */
    private void executeSubActivityGoodsCounts(String agId, int agCount) {
        int count = this.activityGoodsDao.executeSubCount(agId, agCount);
        if (count < 1) {
            throw new SystemException("亲，你来晚了哦，秒杀已经被抢光了！");
        }
    }

    /**
     * 根据orderAndOrderDetailInfos，获取ping++支付的必要信息
     *
     * @param orderAndOrderDetailInfos
     * @return
     */
    private ChargeInfo getChargeInfo(List<OrderAndOrderDetailInfo> orderAndOrderDetailInfos, String clientIp) {
        String payType = null;
        String pingPPorderNo = null;
        long amount = 0;
        String orderName = "";

        OrderAndOrderDetailInfo firstOrderAndOrderDetailInfo = orderAndOrderDetailInfos.get(0);
        payType = firstOrderAndOrderDetailInfo.getOrder().getPayType();
        pingPPorderNo = firstOrderAndOrderDetailInfo.getOrder().getPingPPorderNo();
        for (OrderAndOrderDetailInfo orderAndOrderDetailInfo : orderAndOrderDetailInfos) {
            orderName += orderAndOrderDetailInfo.getOrder().getOrderName() + "；";
            amount = (long) (orderAndOrderDetailInfo.getOrder().getOrderTotal() * 100 + amount);
        }

        ChargeInfo chargeInfo = new ChargeInfo();
        chargeInfo.setChannel(payType);//支付方式 "wx"或"alipay"
        chargeInfo.setOrderNo(pingPPorderNo);//支付订单号
        chargeInfo.setAmount(amount);//支付的金额，以分为单位
        chargeInfo.setClientIp(clientIp); //客户端ip地址
        chargeInfo.setSubject(orderName.length() > 32 ? orderName.substring(0, 32) : orderName);  //商品名
        chargeInfo.setBody(orderName.length() > 100 ? orderName.substring(0, 100) : orderName);  //商品介绍
        return chargeInfo;
    }

    /**
     * 根据订单来获取ping++支付的必要信息
     *
     * @param order
     * @param clientIp
     * @return
     */
    private ChargeInfo getChargeInfo(Order order, String clientIp) {
        long currentTime = System.currentTimeMillis();
        String pingPPorderNo = Constant.BEGIN_CODE_PINGPP_ORDER + GeneratedNRandom.generated(2) + currentTime;
        ChargeInfo chargeInfo = new ChargeInfo();
        chargeInfo.setChannel(order.getPayType()); //支付方式 "wx"或"alipay"
        chargeInfo.setOrderNo(pingPPorderNo); //支付订单号
        chargeInfo.setAmount((long) (order.getOrderTotal() * 100)); //支付的金额，以分为单位
        chargeInfo.setClientIp(clientIp); //客户端ip地址
        chargeInfo.setSubject(order.getOrderName().length() > 32 ? order.getOrderName().substring(0, 32) : order.getOrderName()); //商品名
        chargeInfo.setBody(order.getOrderName().length() > 100 ? order.getOrderName().substring(0, 100) : order.getOrderName());  //商品介绍
        return chargeInfo;
    }


    /**
     * 根据一些基本信息来获取订单和订单详情
     *
     * @param orderInfos
     * @return
     */
    private List<OrderAndOrderDetailInfo> getOrderAndOrderDetailInfoList(OrderInfo... orderInfos) {
        List<OrderAndOrderDetailInfo> orderAndOrderDetailInfos = new ArrayList<>();
        if (orderInfos == null || orderInfos.length <= 0) {
            throw new SystemException("系统异常，请传入正确的订单信息！");
        }

        if (orderInfos.length > 1) { //是批量订单支付
            for (OrderInfo orderInfo : orderInfos) {
                if (orderInfo.isSeckillOrder()) {
                    throw new SystemException("秒杀商品不能批量支付！");
                }
                orderAndOrderDetailInfos.add(this.createOrderAndOrderDetailInfoByOrderInfo(orderInfo));
            }
        } else {//不是批量订单
            orderAndOrderDetailInfos.add(this.createOrderAndOrderDetailInfoByOrderInfo(orderInfos[0]));
        }

        long currentTime = System.currentTimeMillis();
        String pingPPorderNo = Constant.BEGIN_CODE_PINGPP_ORDER + GeneratedNRandom.generated(2) + currentTime;
        for (OrderAndOrderDetailInfo orderAndOrderDetailInfo : orderAndOrderDetailInfos) {
            orderAndOrderDetailInfo.getOrder().setPingPPorderNo(pingPPorderNo);
        }
        return orderAndOrderDetailInfos;
    }

    /**
     * 根据orderInfo获取 一条订单 及该订单对应的订单详情
     *
     * @param orderInfo
     * @return
     */
    private OrderAndOrderDetailInfo createOrderAndOrderDetailInfoByOrderInfo(OrderInfo orderInfo) {
        IQueryParams queryParams = null;
        long currentTime = System.currentTimeMillis();
        Order order = new Order();
        order.setOrderCode(Constant.BEGIN_CODE_ORDER + GeneratedNRandom.generated(3) + currentTime);
        order.setCreateDate(new Date());
        order.setOrderState(Constant.ORDER_STATE_NO_PAYMENT);
        order.setDeleteState(false);
        order.setOrderTotal(Float.parseFloat(orderInfo.getOrderTotal() + ""));
        order.setOriginalOrderTotal(order.getOrderTotal());
        order.setActivityGoods(orderInfo.isSeckillOrder() ? new ActivityGoods(orderInfo.getActivityGodosId()) : null);
        order.setPayType(orderInfo.getPayType());
        order.setReceiverName(orderInfo.getReceiverName());
        order.setReceiverPhone(orderInfo.getReceiverPhone());
        order.setReceiverAdd(orderInfo.getReceiverAdd());
        order.setUserMsg(orderInfo.getUserMsg());

        List<OrderDetail> orderDetails = new ArrayList<>();
        List<Float> carriages = new ArrayList<>();
        List<String> coverImgIds = new ArrayList<>();
        String merchantId = null;
        String orderName = "";
        double orderTotal = 0;
        for (OrderDetailInfo orderDetailInfo : orderInfo.getOrderDetails()) {
            String[] selectors = new String[]{"goodsName", "onsale", "deleteState", "coverImg.imgInfoId", "carriage", "merchant.merchantId"};
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("goodsId", orderDetailInfo.getGoodsId());
            List<Object[]> oGoodsInfos = this.goodsDao.getProperties(selectors, null, queryParams);
            if (oGoodsInfos == null || oGoodsInfos.size() == 0) {
                throw new SystemException("提交订单失败，不存在该商品！");
            }
            Object[] oGoodsInfo = oGoodsInfos.get(0);
            String goodsName = (String) oGoodsInfo[0];
            boolean onsale = (boolean) oGoodsInfo[1];
            boolean deleteState = (boolean) oGoodsInfo[2];
            String coverImgId = (String) oGoodsInfo[3];
            float carriage = Float.parseFloat(oGoodsInfo[4].toString());
            String _merchantId = (String) oGoodsInfo[5];

            coverImgIds.add(coverImgId);
            carriages.add(carriage);
            orderName += goodsName + "；";

            //判断同一订单中用户购买的商品是否属于同一个商家
            if (merchantId != null && !merchantId.equals(_merchantId)) {
                throw new SystemException("同一订单中，只能购买同一个商家的商品！");
            }
            if (merchantId == null) {
                merchantId = _merchantId;
            }

            if (!onsale) {
                throw new SystemException("提交订单失败，'" + goodsName + "'已下架，不支持购买");
            }
            if (deleteState) {
                throw new SystemException("提交订单失败，'" + goodsName + "已被删除，不支持购买");
            }

            Sku sku = this.skuDao.findEntityById(orderDetailInfo.getSkuId());
            if (sku == null) {
                throw new SystemException("提交订单失败，不存在该商品！");
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setGoods(new Goods(orderDetailInfo.getGoodsId()));
            orderDetail.setGoodsCount(orderDetailInfo.getCount());
            orderDetail.setPrice(Float.parseFloat(sku.getPrice() + ""));
            orderDetail.setSkuId(sku.getSkuId());
            if (!orderInfo.isSeckillOrder()) {
                if (sku.getCurrentStock() < orderDetailInfo.getCount()) {
                    throw new SystemException("提交订单失败，'" + goodsName + "'库存不足！");
                }
                orderTotal += sku.getPrice() * orderDetailInfo.getCount();
            } else {
                this.checkSecKillGoods(orderInfo.getActivityGodosId(), orderInfo.getUserId(), orderDetailInfo.getCount());
                float agPrice = this.activityGoodsService.getActivityGoodsPriceById(orderInfo.getActivityGodosId());
                orderDetail.setPrice(NumberUtils.floatScale(2, agPrice));
                orderTotal += agPrice * orderDetailInfo.getCount();
            }
            String skuName = this.goodsAttributeService.getSkuNameBySkuId(sku.getSkuId());
            orderDetail.setSkuName(skuName);

            orderDetails.add(orderDetail);
        }
        order.setMerchant(new Merchant(merchantId));
        order.setCoverImg(coverImgIds.isEmpty() ? null : new ImgInfo(coverImgIds.get(0)));
        orderTotal = NumberUtils.doubleScale(2, orderTotal + Collections.max(carriages));
        order.setCarriage(NumberUtils.floatScale(2, Collections.max(carriages)));
        order.setOrderName(orderName.length() > 200 ? orderName.substring(0, 200) : orderName);
        orderInfo.setOrderTotal(NumberUtils.doubleScale(2, orderInfo.getOrderTotal()));
        if (orderInfo.getOrderTotal() != orderTotal) {
            throw new SystemException("订单提交失败,提交的订单金额与实际金额不符！");
        }
        OrderAndOrderDetailInfo orderAndOrderDetailInfo = new OrderAndOrderDetailInfo(order);
        orderAndOrderDetailInfo.setOrderDetails(orderDetails);
        return orderAndOrderDetailInfo;
    }

    /**
     * 根据活动商品Id，用户Id，和购买的数量，判断该用户是否可以购买该活动商品
     *
     * @param activityGoodsId
     * @param userId
     * @param agCount
     */
    private void checkSecKillGoods(String activityGoodsId, String userId, int agCount) {

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓判断买家在本次活动中该秒杀商品是否已经下过订单↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("user.userId", userId)
                .andEqual("activityGoods.agId", activityGoodsId)
                .andEqual("deleteState", false);
        List<Object> orderIds = this.orderDao.getProperty("orderId", null, queryParams);
        if (orderIds.size() > 0) {
            throw new StatusCodeException("一次活动中同一件秒杀商品只能下一次订单，你已经抢购过此秒杀商品；如还未支付，请到我的订单中支付。！", ResponseStatusCode.REPEAT_SECKILL_ORDER);
        }
         /*↑↑↑↑↑↑↑↑↑↑↑↑↑判断买家在本次活动中该秒杀商品是否已经下过订单↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓判断是否在活动期间↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("agId", activityGoodsId);
        String[] params = new String[]{"activity.beginDate", "activity.endDate"};
        List<Object[]> queryResult = this.activityGoodsDao.getProperties(params, null, queryParams);
        if (queryResult.size() <= 0) {
            throw new SystemException("找不到对应的活动商品");
        }
        Date activityStartDate = (Date) queryResult.get(0)[0]; //活动开始时间
        Date activityEndDate = (Date) queryResult.get(0)[1];  //活动结束时间
        Date now = new Date();
        if (activityStartDate.after(now)) {
            throw new SystemException("亲！不要着急哦，活动还未开始呢！");
        }
        if (activityEndDate.before(now)) {
            throw new SystemException("亲！很遗憾，活动已经结束了！");
        }
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑判断是否在活动期间↑↑↑↑↑↑↑↑↑↑↑↑↑*/

        /*↓↓↓↓↓↓↓↓↓↓↓↓↓判断活动商品的剩余量是否足够买家购买↓↓↓↓↓↓↓↓↓↓↓↓↓*/
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("agId", activityGoodsId);
        List<Object> oActivityGoodsCount = this.activityGoodsDao.getProperty("agCount", null, queryParams);
        int agSurplus = (int) oActivityGoodsCount.get(0);
        if (agSurplus <= 0) {
            throw new StatusCodeException("亲，你来晚了哦，秒杀已经被抢光了！", ResponseStatusCode.ALREADY_DRAW_OVER);
        }
        if (agSurplus - agCount < 0) {
            throw new SystemException("亲，现在库存只有" + agSurplus + "件" + "不够你购买啦！");
        }
        /*↑↑↑↑↑↑↑↑↑↑↑↑↑判断活动商品的剩余量是否足够买家购买↑↑↑↑↑↑↑↑↑↑↑↑↑*/
    }
}

