package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.MerchantBill;
import cn.com.zhihetech.online.vo.ExportMerchantBill;
import cn.com.zhihetech.online.vo.MerchantBillSearch;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
public interface IMerchantBillService extends SupportService<MerchantBill> {

    PageData<MerchantBill> getSearchPageData(Pager pager, IQueryParams queryParams);

    long getTotal(IQueryParams queryParams);

    List<Object> getPropertyByQueryParam(String selector, Pager pager, IQueryParams queryParams);

    void saveMerchantBills(String merchantId, Date startDate, Date endDate);

    List<ExportMerchantBill> getExportMerchantBill(Pager pager, IQueryParams queryParams);

    void executeHandleBill(MerchantBill merchantBill);

    /**
     * 获取商家结算单（账单）批次号
     *
     * @return
     */
    List<Map<String, String>> getMerchantBillBatchCodes();

    /**
     * 根据账单批次和支付方式获取支付的最小时间和最大时间
     *
     * @param batchCode
     * @param payType
     */
    Map<String, String> getMinAndMaxPayDateByBatchCodeAndPayType(String batchCode, String payType);
}
