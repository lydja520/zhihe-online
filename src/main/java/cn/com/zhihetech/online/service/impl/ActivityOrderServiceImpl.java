package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityOrderService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.alipay.AlipaySubmit;
import cn.com.zhihetech.online.vo.ActivityOrderAndActivityOrderDeatils;
import cn.com.zhihetech.util.common.GeneratedNRandom;
import cn.com.zhihetech.util.hibernate.*;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ydc on 16-5-30.
 */
@Service(value = "activityOrderService")
public class ActivityOrderServiceImpl implements IActivityOrderService {

    @Resource(name = "activityOrderDao")
    private IActivityOrderDao activityOrderDao;
    @Resource(name = "merchantAllianceDao")
    private IMerchantAllianceDao merchantAllianceDao;
    @Resource(name = "activityOrderDetailDao")
    private IActivityOrderDetailDao activityOrderDetailDao;
    @Resource(name = "redEnvelopDao")
    private IRedEnvelopDao redEnvelopDao;
    @Resource(name = "activityDao")
    private IActivityDao activityDao;
    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;

    @Override
    public ActivityOrder getById(String id) {
        return this.activityOrderDao.findEntityById(id);
    }

    @Override
    public void delete(ActivityOrder activityOrder) {

    }

    @Override
    public ActivityOrder add(ActivityOrder activityOrder) {
        return null;
    }

    @Override
    public void update(ActivityOrder activityOrder) {

    }

    @Override
    public List<ActivityOrder> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<ActivityOrder> getPageData(Pager pager, IQueryParams queryParams) {
        return this.activityOrderDao.getPageData(pager, queryParams);
    }

    /**
     * 活动订单提交
     *
     * @param merchantId
     * @param activityId
     * @return
     */
    @Override
    public ActivityOrderAndActivityOrderDeatils executeQueryOrder(String merchantId, String activityId) {
        ActivityOrderAndActivityOrderDeatils activityOrderAndActivityOrderDeatils = new ActivityOrderAndActivityOrderDeatils();
        List<ActivityOrderDetail> activityOrderDetails = new ArrayList<>();
        Activity activity = this.activityDao.findEntityById(activityId);
        if (activity == null) {
            throw new SystemException("不存在该活动");
        }
        if (activity.getCurrentState() != Constant.ACTIVITY_STATE_UNSBUMIT) {
            throw new SystemException("该活动状态不支持提交操作，请刷新页面后确认活动状态后在试！");
        }
        Merchant merchant = this.merchantDao.findEntityById(merchantId);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activityId", activityId)
                .andEqual("merchantId", merchantId)
                .andEqual("orderType", ActivityOrderDetail.OrderType.activityCost);
        List<ActivityOrderDetail> activityOrderDetails1 = this.activityOrderDetailDao.getEntities(queryParams);
        ActivityOrderDetail activityCostOrderDetail = null;
        if (activityOrderDetails1.size() <= 0) {
            ActivityOrderDetail activityOrderDetail = new ActivityOrderDetail();
            activityOrderDetail.setActivityOrder(null);
            activityOrderDetail.setOrderType(ActivityOrderDetail.OrderType.activityCost);
            activityOrderDetail.setPayState(false);
            activityOrderDetail.setMoney(Constant.MERCHNT_ACTIVITY_BUDGET);
            activityOrderDetail.setOrderTypeId(merchantId);
            activityOrderDetail.setMerchantId(merchantId);
            activityOrderDetail.setActivityId(activityId);
            this.activityOrderDetailDao.saveEntity(activityOrderDetail);
            activityCostOrderDetail = activityOrderDetail;
        } else {
            activityCostOrderDetail = activityOrderDetails1.get(0);
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("activityId", activityId)
                .andEqual("merchantId", merchantId)
                .andEqual("payState", false)
                .sort("orderType", Order.ASC);
        List<ActivityOrderDetail> activityOrderDetails2 = this.activityOrderDetailDao.getEntities(queryParams);
        activityOrderDetails.addAll(activityOrderDetails2);
        if (activityOrderDetails.size() == 0) {
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("activity.activitId", activityId).andEqual("merchant.merchantId", merchantId);
            List<MerchantAlliance> merchantAlliances = this.merchantAllianceDao.getEntities(queryParams);
            MerchantAlliance merchantAlliance = merchantAlliances.get(0);
            if (merchantAlliance.getAllianceState() != MerchantAlliance.ALLIANCE_READINESS_STATE) {
                merchantAlliance.setAllianceState(MerchantAlliance.ALLIANCE_READINESS_STATE);
                this.merchantAllianceDao.updateEntity(merchantAlliance);
            }
            return null;
        }
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("activityId", activityId)
                .andEqual("merchantId", merchantId)
                .andEqual("payState", false)
                .sort("orderType", Order.ASC);
        List<Object> objects = this.activityOrderDetailDao.getProperty("sum(money)", null, queryParams);
        double totalMoney = (double) objects.get(0);
        totalMoney = new BigDecimal(totalMoney).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (totalMoney <= 0.0f) {
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("activity.activitId", activityId).andEqual("merchant.merchantId", merchantId);
            List<MerchantAlliance> merchantAlliances = this.merchantAllianceDao.getEntities(queryParams);
            MerchantAlliance merchantAlliance = merchantAlliances.get(0);
            if (merchantAlliance.getAllianceState() != MerchantAlliance.ALLIANCE_READINESS_STATE) {
                merchantAlliance.setAllianceState(MerchantAlliance.ALLIANCE_READINESS_STATE);
                this.merchantAllianceDao.updateEntity(merchantAlliance);
            }
            activityCostOrderDetail.setPayState(true);
            this.activityOrderDetailDao.updateEntity(activityCostOrderDetail);
            return null;
        }
        Date now = new Date();
        ActivityOrder activityOrder = new ActivityOrder();
        activityOrder.setActivity(activity);
        activityOrder.setMerchant(merchant);
        activityOrder.setCreateDate(now);
        activityOrder.setOrderCode(Constant.BEGIN_CODE_ACTIVITY_ORDER + GeneratedNRandom.generated(3) + System.currentTimeMillis());
        activityOrder.setOrderName(merchant.getMerchName() + "活动订单" + DateUtils.formatDateTime(now));
        activityOrder.setTotalMoney(totalMoney);
        activityOrder.setPayState(false);
        this.activityOrderDao.saveEntity(activityOrder);

        for (ActivityOrderDetail activityOrderDetail : activityOrderDetails) {
            activityOrderDetail.setActivityOrder(activityOrder);
            this.activityOrderDetailDao.updateEntity(activityOrderDetail);
        }
        activityOrderAndActivityOrderDeatils.setActivityOrderDetails(activityOrderDetails);
        activityOrderAndActivityOrderDeatils.setActivityOrder(activityOrder);
        return activityOrderAndActivityOrderDeatils;
    }

    /**
     * 执行商家活动账单支付
     *
     * @param activitOrderId
     * @return
     */
    @Override
    public String executePay(String activitOrderId) {
        ActivityOrder activityOrder = this.activityOrderDao.findEntityById(activitOrderId);
        if (activityOrder == null) {
            throw new SystemException("不存在该活动订单");
        }
        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AppConfig.AliPayConfig.service);
        sParaTemp.put("partner", AppConfig.AliPayConfig.partner);
        sParaTemp.put("seller_id", AppConfig.AliPayConfig.seller_id);
        sParaTemp.put("_input_charset", AppConfig.AliPayConfig.input_charset);
        sParaTemp.put("payment_type", AppConfig.AliPayConfig.payment_type);
        sParaTemp.put("notify_url", AppConfig.AliPayConfig.notify_url);
        sParaTemp.put("return_url", AppConfig.AliPayConfig.return_url);
        sParaTemp.put("anti_phishing_key", AppConfig.AliPayConfig.anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", AppConfig.AliPayConfig.exter_invoke_ip);
        sParaTemp.put("out_trade_no", activityOrder.getOrderCode());
        sParaTemp.put("subject", activityOrder.getOrderName());
        sParaTemp.put("total_fee", activityOrder.getTotalMoney() + "");
        sParaTemp.put("body", activityOrder.getExtMsg());
        //其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
        //如sParaTemp.put("参数名","参数值");

        //建立请求
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
        return sHtmlText;
    }

    /**
     * 支付成功后，更新活动订单状态，更新对应的活动订单详情支付状态，更新红包支付状态
     *
     * @param out_trade_no
     * @param trade_no
     * @param seller_email
     * @param buyer_email
     */
    @Override
    public void executePayResult(String out_trade_no, String trade_no, String seller_email, String buyer_email, String total_fee) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activityOrder.orderCode", out_trade_no);
        List<ActivityOrderDetail> activityOrderDetails = this.activityOrderDetailDao.getEntities(null, queryParams);
        if (activityOrderDetails.size() <= 0) {
            throw new SystemException("系统出现异常，请与管理员联系！");
        }
        Date now = new Date();
        ActivityOrder activityOrder = activityOrderDetails.get(0).getActivityOrder();
        if (activityOrder.isPayState()) {
            throw new SystemException("该订单已经支付成功了，请尽快回实淘管理系统内刷新页面查看！");
        }
        activityOrder.setPayState(true);
        activityOrder.setPayDate(now);
        activityOrder.setAliTransactionCode(trade_no);
        activityOrder.setSellerAccount(seller_email);
        activityOrder.setBuyerAccount(buyer_email);
        activityOrder.setTotalMoney(Float.parseFloat(total_fee));
        this.activityOrderDao.updateEntity(activityOrder);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("payState", true);
        for (ActivityOrderDetail activityOrderDetail : activityOrderDetails) {
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("activityOrderDetailId", activityOrderDetail.getActivityOrderDetailId());
            this.activityOrderDetailDao.executeUpdate(paramAndValue, queryParams);
            if (activityOrderDetail.getOrderType() == ActivityOrderDetail.OrderType.redEvelop) {
                String redEvelopId = activityOrderDetail.getOrderTypeId();
                queryParams = new GeneralQueryParams();
                queryParams.andEqual("envelopId", redEvelopId);
                this.redEnvelopDao.executeUpdate(paramAndValue, queryParams);
            }
        }

        queryParams = new GeneralQueryParams();
        queryParams.andEqual("activity.activitId", activityOrder.getActivity().getActivitId())
                .andEqual("merchant.merchantId", activityOrder.getMerchant().getMerchantId());
        List<Object> objects = this.merchantAllianceDao.getProperty("activityBudget", null, queryParams);
        if (objects.size() == 0) {
            throw new SystemException("系统出现异常，请与管理员联系！");
        }
        float activityBudget = (float) objects.get(0);
        activityBudget = new BigDecimal(activityBudget + Float.parseFloat(total_fee)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        paramAndValue.clear();
        paramAndValue.put("activityBudget", activityBudget);
        this.merchantAllianceDao.executeUpdate(paramAndValue, queryParams);

        Activity activity = activityOrder.getActivity();
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("activitId", activity.getActivitId());
        float totalActivityBudget = activity.getActivityBudget();
        totalActivityBudget = new BigDecimal(totalActivityBudget + Float.parseFloat(total_fee)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        paramAndValue.put("activityBudget", totalActivityBudget);
        this.activityDao.executeUpdate(paramAndValue, queryParams);
        queryParams = new GeneralQueryParams();
        queryParams.andEqual("activity.activitId", activityOrder.getActivity().getActivitId())
                .andEqual("merchant.merchantId", activityOrder.getMerchant().getMerchantId());
        List<MerchantAlliance> merchantAlliances = this.merchantAllianceDao.getEntities(queryParams);
        MerchantAlliance merchantAlliance = merchantAlliances.get(0);
        if (merchantAlliance.getAllianceState() != MerchantAlliance.ALLIANCE_READINESS_STATE) {
            merchantAlliance.setAllianceState(MerchantAlliance.ALLIANCE_READINESS_STATE);
            this.merchantAllianceDao.updateEntity(merchantAlliance);
        }
    }

    /**
     * @param out_trade_no
     * @param trade_no
     * @param seller_email
     * @param buyer_email
     * @param total_fee
     */
    @Override
    public void executePayResultConfirm(String out_trade_no, String trade_no, String seller_email, String buyer_email, String total_fee) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("orderCode", out_trade_no);
        List<ActivityOrder> activityOrders = this.activityOrderDao.getEntities(queryParams);
        if (activityOrders.size() == 0) {
            throw new SystemException("系统出现异常，请与管理员联系！");
        }
        ActivityOrder activityOrder = activityOrders.get(0);
        if (!activityOrder.isPayState()) {
            this.executePayResult(out_trade_no, trade_no, seller_email, buyer_email, total_fee);
        }
    }

    @Override
    public ActivityOrderAndActivityOrderDeatils executeQueryOrderByOrderId(String activityOrderId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activityOrder.activityOrderId", activityOrderId);
        List<ActivityOrderDetail> activityOrderDetails = this.activityOrderDetailDao.getEntities(queryParams);
        if (activityOrderDetails.size() <= 0) {
            throw new SystemException("系统出现异常，请与管理员联系！");
        }
        ActivityOrderAndActivityOrderDeatils activityOrderAndActivityOrderDeatils = new ActivityOrderAndActivityOrderDeatils();
        activityOrderAndActivityOrderDeatils.setActivityOrder(activityOrderDetails.get(0).getActivityOrder());
        activityOrderAndActivityOrderDeatils.setActivityOrderDetails(activityOrderDetails);
        return activityOrderAndActivityOrderDeatils;
    }

    @Override
    public List<Object> getProperty(String selector, Pager pager, IQueryParams queryParams) {
        return this.activityOrderDao.getProperty(selector, pager, queryParams);
    }
}
