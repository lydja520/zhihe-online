package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.WebChineseConfig;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IMerchantBillService;
import cn.com.zhihetech.online.service.IMerchantService;
import cn.com.zhihetech.online.service.IOrderService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.PropertiesUtils;
import cn.com.zhihetech.online.util.RealAmountUtils;
import cn.com.zhihetech.online.util.SMSUtils;
import cn.com.zhihetech.online.vo.ExportMerchantBill;
import cn.com.zhihetech.online.vo.StartDateAndEndDate;
import cn.com.zhihetech.util.common.GeneratedNRandom;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
@Service(value = "merchantBillService")
public class MerchantBillServiceImpl implements IMerchantBillService {

    @Resource(name = "merchantBillDao")
    private IMerchantBillDao merchantBillDao;
    @Resource
    private IMerchantBillDetailDao merchantBillDetailDao;
    @Resource(name = "orderDao")
    private IOrderDao orderDao;
    @Resource(name = "merchantBilErrRecordDao")
    private IMerchantBillErrRecordDao merchantBillErrRecordDao;
    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;

    @Override
    public MerchantBill getById(String id) {
        return this.merchantBillDao.findEntityById(id);
    }

    @Override
    public void delete(MerchantBill merchantBill) {

    }

    @Override
    public MerchantBill add(MerchantBill merchantBill) {
        return null;
    }


    /**
     * @param merchantId
     * @param startDate  账单的开始时间
     * @param endDate    账单的结束时间
     */
    @Override
    public void saveMerchantBills(String merchantId, Date startDate, Date endDate) {
        Date now = new Date();
        IQueryParams queryParams = null;
        if (StringUtils.isEmpty(merchantId)) {  //生成startDate和endDate之间所有商家的账单
            List<String> merchantIds = this.merchantBillDetailDao.getMerchantIds(startDate, endDate);
            if (merchantIds == null || merchantIds.size() <= 0) {
                return;
            }
            for (String _merchantId : merchantIds) {
                Merchant merchant = new Merchant();
                merchant.setMerchantId(_merchantId);
                try {
                    this.generateMerchantBill(merchant, true, startDate, endDate);
                } catch (Exception e) {
                    MerchantBillErrRecord merchantBillErrRecord = new MerchantBillErrRecord();
                    merchantBillErrRecord.setMerchant(merchant);
                    merchantBillErrRecord.setStartDate(startDate);
                    merchantBillErrRecord.setEndDate(endDate);
                    merchantBillErrRecord.setErrMsg(e.toString());
                    merchantBillErrRecord.setCreateDate(now);
                    merchantBillErrRecord.setHandleState(false);
                    merchantBillErrRecord.setBatchCode("A" + DateUtils.formatDate(now, "yyyyMMdd"));
                    this.merchantBillErrRecordDao.saveEntity(merchantBillErrRecord);
                }
            }
        } else {  //生成对应商家在startDate和endDate的账单
            Merchant merchant = new Merchant();
            merchant.setMerchantId(merchantId);
            try {
                this.generateMerchantBill(merchant, false, startDate, endDate);
            } catch (Exception e) {
                throw new SystemException("账单未生成，系统错误，请与管理员联系！", e);
            }
        }
    }

    /**
     * 单个商家账单处理
     *
     * @param merchant
     * @param isAutoGenerate 是系统自动生成结算单，还是手动生成
     * @param startDate      //账单开始时间
     * @param endDate        //账单结束时间
     * @throws Exception
     */
    private void generateMerchantBill(Merchant merchant, boolean isAutoGenerate, Date startDate, Date endDate) throws Exception {
        Date nowDateTime = new Date();
        MerchantBill merchantBill = new MerchantBill();
        merchantBill.setCreateDate(nowDateTime);
        merchantBill.setMerchant(merchant);

        IQueryParams queryParams = new GeneralQueryParams().andEqual("merchantId", merchant.getMerchantId());
        List<Object> aliAccounts = this.merchantDao.getProperty("alipayCode", null, queryParams);
        if (aliAccounts != null && !aliAccounts.isEmpty()) {
            merchantBill.setAliPayAccount(aliAccounts.get(0).toString());
        }

        queryParams = new GeneralQueryParams();
        queryParams.andEqual("order.merchant.merchantId", merchant.getMerchantId())
                .andEqual("handleState", false)
                .andMoreAndEq("createDate", startDate)
                .andLessAndEq("createDate", endDate);
        List<Object> amountObj = this.merchantBillDetailDao.getProperty("sum(realAmount)", null, queryParams);
        if (amountObj == null || amountObj.isEmpty() || amountObj.get(0) == null) {
            return;
        }
        double amount = (double) amountObj.get(0);
        amount = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        merchantBill.setAmount(amount);
        merchantBill.setPoundage(RealAmountUtils.calculatePoundage(amount, Constant.MERCHANT_BILL_POUNDAGE_RATE));
        merchantBill.setRealAmount(RealAmountUtils.realAmount(amount, Constant.MERCHANT_BILL_POUNDAGE_RATE));
        merchantBill.setBillCode(Constant.BEGIN_CODE_MERCHANT_BILL + GeneratedNRandom.generated(2) + System.currentTimeMillis());
        merchantBill.setOrderAmount(this.merchantBillDetailDao.getRecordTotal(queryParams));
        merchantBill.setHandleState(false);
        merchantBill.setStartDate(startDate);
        merchantBill.setEndDate(endDate);
        merchantBill.setBatchCode("A" + DateUtils.formatDate(nowDateTime, "yyyyMMdd"));
        merchantBill.setAutoGenerate(isAutoGenerate);
        merchantBillDao.saveEntity(merchantBill);

        List<MerchantBillDetail> merchantBillDetails = this.merchantBillDetailDao.getEntities(queryParams);
        for (MerchantBillDetail merchantBillDetail : merchantBillDetails) {
            merchantBillDetail.setMerchantBill(merchantBill);
            merchantBillDetail.setHandleState(true);
            this.merchantBillDetailDao.updateEntity(merchantBillDetail);
            cn.com.zhihetech.online.bean.Order order = merchantBillDetail.getOrder();
            order.setOrderState(Constant.ORDER_STATE_ALREADY_GENERATE_BILL);
            this.orderDao.updateEntity(order);
        }
    }

    @Override
    public List<ExportMerchantBill> getExportMerchantBill(Pager pager, IQueryParams queryParams) {
        long total = this.merchantBillDao.getRecordTotal(queryParams);
        if (total > Integer.parseInt(PropertiesUtils.getProperties().getProperty("EXPORT_DATA_MAX_RECORD"))) {
            throw new SystemException("根据当前查询条件查询的数据太多，无法生成excel表格！请按精确的查询条件以减少数据条数！");
        }
        List<MerchantBill> merchantBills = this.merchantBillDao.getEntities(queryParams);
        List<ExportMerchantBill> exportMerchantBills = new LinkedList<>();
        for (MerchantBill merchantBill : merchantBills) {
            ExportMerchantBill exportMerchantBill = new ExportMerchantBill();
            exportMerchantBill.setMerchantName(merchantBill.getMerchant().getMerchName());
            exportMerchantBill.setCreateDate(DateUtils.formatDateTime(merchantBill.getCreateDate()));
            String startDateAndEndDateStr = DateUtils.formatDateTime(merchantBill.getStartDate()) + "~" +
                    DateUtils.formatDateTime(merchantBill.getEndDate());
            exportMerchantBill.setStartAndEndDate(startDateAndEndDateStr);
            exportMerchantBill.setBillCode(merchantBill.getBillCode());
            exportMerchantBill.setAmount(merchantBill.getAmount() + "");
            exportMerchantBill.setPoundage(merchantBill.getPoundage() + "");
            exportMerchantBill.setRealAmount(merchantBill.getRealAmount() + "");
            exportMerchantBill.setPoundatgeRate(merchantBill.getHandlePoundageRate() + "");
            //exportMerchantBill.setMerchantAliCode(merchantBill.getMerchant().getAlipayCode());
            exportMerchantBill.setMerchantAliCode(merchantBill.getAliPayAccount());
            exportMerchantBill.setHandleState(merchantBill.isHandleState() ? "是" : "否");
            exportMerchantBills.add(exportMerchantBill);
        }
        return exportMerchantBills;
    }

    /**
     * 实淘处理账单
     *
     * @param merchantBill
     */
    @Override
    public void update(MerchantBill merchantBill) {
    }

    @Override
    public void executeHandleBill(MerchantBill merchantBill) {

        if (StringUtils.isEmpty(merchantBill.getAliPayTransactionCode())) {
            throw new SystemException("支付宝交易号不能为空!");
        }
        MerchantBill _merchantBill = this.merchantBillDao.findEntityById(merchantBill.getBillId());
        if (_merchantBill == null) {
            throw new SystemException("该账单不存在");
        }
        if (_merchantBill.isHandleState()) {
            throw new SystemException("该账单已近处理过，无需再次处理！");
        }
        Date now = new Date();
        _merchantBill.setAliPayTransactionCode(merchantBill.getAliPayTransactionCode());
        _merchantBill.setHandleDate(now);
        _merchantBill.setHandleState(true);
        this.merchantBillDao.updateEntity(_merchantBill);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchantBill.billId", _merchantBill.getBillId());
        List<Object> objects = this.merchantBillDetailDao.getProperty("order.orderId", null, queryParams);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("orderState", Constant.ORDER_STATE_ALREADY_BILL);
        for (Object orderIdStr : objects) {
            String orderId = (String) orderIdStr;
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("orderId", orderId);
            this.orderDao.executeUpdate(paramAndValue, queryParams);
        }
        String msgTxt = MessageFormat.format(WebChineseConfig.MsgTemplate.ORDER_ALREADY_BILL, _merchantBill.getBillCode());
        SMSUtils.asyncSendSMS(_merchantBill.getMerchant().getContactMobileNO(), msgTxt);    //异步发送短信通知
    }

    @Override
    public List<MerchantBill> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<MerchantBill> getPageData(Pager pager, IQueryParams queryParams) {
        return this.merchantBillDao.getPageData(pager, queryParams);
    }

    @Override
    public PageData<MerchantBill> getSearchPageData(Pager pager, IQueryParams queryParams) {
        return this.merchantBillDao.getPageData(pager, queryParams);
    }

    @Override
    public long getTotal(IQueryParams queryParams) {
        return this.merchantBillDao.getRecordTotal(queryParams);
    }

    @Override
    public List<Object> getPropertyByQueryParam(String selector, Pager pager, IQueryParams queryParams) {
        return this.merchantBillDao.getProperty(selector, pager, queryParams);
    }

    /**
     * 获取商家结算单（账单）批次号
     *
     * @return 账单（结算单）批次号
     */
    @Override
    public List<Map<String, String>> getMerchantBillBatchCodes() {
        List<Object> tmps = this.merchantBillDao.getProperty("distinct(batchCode)", null, null);
        List<Map<String, String>> batchCodes = new ArrayList<>();
        if (tmps == null || tmps.isEmpty()) {
            return batchCodes;
        }
        for (Object obj : tmps) {
            Map<String, String> map = new HashMap<>();
            map.put("value", obj.toString());
            map.put("text", obj.toString());
            batchCodes.add(map);
        }
        return batchCodes;
    }

    /**
     * 根据账单批次和支付方式获取支付的最小时间和最大时间
     *
     * @param batchCode
     * @param payType
     */
    @Override
    public Map<String, String> getMinAndMaxPayDateByBatchCodeAndPayType(String batchCode, String payType) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("batchCode", batchCode);
        List<Object> billIds = this.merchantBillDao.getProperty("DISTINCT(billId)", null, queryParams);
        if (billIds == null || billIds.isEmpty()) {
            throw new SystemException("未找到对应批次的账单！");
        }
        queryParams = new GeneralQueryParams().andIn("merchantBill.billId", billIds);
        List<Object> orderIds = this.merchantBillDetailDao.getProperty("order.orderId", null, queryParams);
        if (orderIds == null || orderIds.isEmpty()) {
            throw new SystemException("未找到对应批次的账单！");
        }
        queryParams = new GeneralQueryParams().andIn("orderId", orderIds).andEqual("payType", payType);
        List<Object[]> dates = this.orderDao.getProperties(new String[]{"min(payDate)", "max(payDate)"}, null, queryParams);
        if (dates == null || dates.isEmpty()) {
            throw new SystemException("未找到对应批次的账单！");
        }
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("min", dates.get(0)[0].toString());
        dateMap.put("max", dates.get(0)[1].toString());
        new Order().getPayDate();
        return dateMap;
    }
}
