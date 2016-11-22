package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.MerchantBillDetail;
import cn.com.zhihetech.online.vo.ExportMercahntBillOrderDetail;
import cn.com.zhihetech.online.vo.ExportMerchantBillOrder;
import cn.com.zhihetech.online.vo.MerchantBillSearch;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by YangDaiChun on 2016/5/9.
 */
public interface IMerchantBillDetailService extends SupportService<MerchantBillDetail>{
    List<ExportMerchantBillOrder> getExportMerchantBillOrder(MerchantBillSearch billSearch);

    List<ExportMercahntBillOrderDetail> getExportMerchantBillOrderDetail(IQueryParams queryParams);
}
