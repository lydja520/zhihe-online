package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.MerchantBillDetail;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IMerchantBillDetailDao;
import cn.com.zhihetech.online.dao.IOrderDetailDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IMerchantBillDetailService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.PropertiesUtils;
import cn.com.zhihetech.online.vo.ExportMercahntBillOrderDetail;
import cn.com.zhihetech.online.vo.ExportMerchantBillOrder;
import cn.com.zhihetech.online.vo.MerchantBillSearch;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/5/9.
 */
@Service(value = "merchantBillDetailService")
public class MerchantBillDetailServiceImpl implements IMerchantBillDetailService {

    @Resource(name = "merchantBillDetailDao")
    private IMerchantBillDetailDao merchantBillDetailDao;
    @Resource(name = "orderDetailDao")
    private IOrderDetailDao orderDetailDao;

    @Override
    public MerchantBillDetail getById(String id) {
        return null;
    }

    @Override
    public void delete(MerchantBillDetail merchantBillDetail) {

    }

    @Override
    public MerchantBillDetail add(MerchantBillDetail merchantBillDetail) {
        return null;
    }

    @Override
    public void update(MerchantBillDetail merchantBillDetail) {

    }

    @Override
    public List<MerchantBillDetail> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<MerchantBillDetail> getPageData(Pager pager, IQueryParams queryParams) {
        return this.merchantBillDetailDao.getPageData(pager, queryParams);
    }

    @Override
    public List<ExportMerchantBillOrder> getExportMerchantBillOrder(MerchantBillSearch billSearch) {
        IQueryParams queryParams = new GeneralQueryParams();
        if (!StringUtils.isEmpty(billSearch.getMerchant().getMerchName())) {
            queryParams.andAllLike("merchantBill.merchant.merchName", billSearch.getMerchant().getMerchName());
        }
        if (!StringUtils.isEmpty(billSearch.getBillCode())) {
            queryParams.andAllLike("merchantBill.billCode", billSearch.getBillCode());
        }
        if (!StringUtils.isEmpty(billSearch.getAliPayTransactionCode())) {
            queryParams.andAllLike("merchantBill.aliPayTransactionCode", billSearch.getAliPayTransactionCode());
        }
        if (!StringUtils.isEmpty(billSearch.getStartTime())) {
            Date _start = DateUtils.getStartDateTimeWithDate(DateUtils.String2Date(billSearch.getStartTime()));
            queryParams.andMoreAndEq("merchantBill.createDate", _start);
        }
        if (!StringUtils.isEmpty(billSearch.getEndTime())) {
            Date _end = DateUtils.getEndDateTimeWithDate(DateUtils.String2Date(billSearch.getEndTime()));
            queryParams.andLessAndEq("merchantBill.createDate", _end);
        }

        queryParams.sort("merchantBill.merchant", Order.DESC);

        long total = this.merchantBillDetailDao.getRecordTotal(queryParams);
        if (total > Integer.parseInt(PropertiesUtils.getProperties().getProperty("EXPORT_DATA_MAX_RECORD"))) {
            throw new SystemException("根据当前查询条件查询的数据太多，无法生成excel表格！请按精确的查询条件以减少数据条数！");
        }
        String[] params = new String[10];
        params[0] = "merchantBill.billCode";
        params[1] = "merchantBill.merchant.merchName";
        params[2] = "order.pingPPorderNo";
        params[3] = "order.payDate";
        params[4] = "order.payType";
        params[5] = "order.activityGoods.agId";
        params[6] = "amount";
        params[7] = "order.carriage";
        params[8] = "poundage";
        params[9] = "realAmount";
        List<Object[]> objects = this.merchantBillDetailDao.getProperties(params, null, queryParams);
        List<ExportMerchantBillOrder> merchantBillOrders = new LinkedList<>();
        for (Object[] object : objects) {
            ExportMerchantBillOrder merchantBillOrder = new ExportMerchantBillOrder();
            merchantBillOrder.setMerchentBillCode(object[0] + "");
            merchantBillOrder.setMerchantName(object[1] + "");
            merchantBillOrder.setOrderCode(object[2] + "");
            merchantBillOrder.setOrderPayDate(object[3] + "");
            merchantBillOrder.setOrderPayType(object[4] + "");
            merchantBillOrder.setActivityGoodsState(object[5] == null ? "否" : "是");
            merchantBillOrder.setOrderAmount(object[6] + "");
            merchantBillOrder.setOrderCarriage(object[7] + "");
            merchantBillOrder.setOrderPoundage(object[8] + "");
            merchantBillOrder.setOrderRealAmount(object[9] + "");
            merchantBillOrder.setOrderPoundageRate(Constant.ORDER_HANDLER_POUNDAGE_RATE + "");
            merchantBillOrders.add(merchantBillOrder);
        }
        return merchantBillOrders;
    }

    @Override
    public List<ExportMercahntBillOrderDetail> getExportMerchantBillOrderDetail(IQueryParams queryParams) {
        String[] params = new String[6];
        params[0] = "order.pingPPorderNo";
        params[1] = "order.user.userPhone";
        params[2] = "order.user.userName";
        params[3] = "order.activityGoods.agId";
        params[4] = "order.carriageNum";
        params[5] = "order.orderId";
        List<Object[]> objects = this.merchantBillDetailDao.getProperties(params, null, queryParams);
        List<ExportMercahntBillOrderDetail> orderDetails = new LinkedList<>();
        for (Object[] _object : objects) {
            ExportMercahntBillOrderDetail orderDetail = new ExportMercahntBillOrderDetail();
            orderDetail.setOrderCode(_object[0] + "");
            orderDetail.setUserPhone(_object[1] + "");
            orderDetail.setUserName(_object[2] + "");
            orderDetail.setAcrivityGoodsState(_object[3] == null ? "否" : "是");
            orderDetail.setCarriageNum(_object[4] + "");
            String orderId = _object[5] + "";
            queryParams = new GeneralQueryParams();
            queryParams.andEqual("order.orderId", orderId);
            params = new String[3];
            params[0] = "goods.goodsName";
            params[1] = "goodsCount";
            params[2] = "price";
            List<Object[]> _objects = this.orderDetailDao.getProperties(params, null, queryParams);
            for (Object[] objects1 : _objects) {
                orderDetail.setGoodsName(objects1[0] + "");
                orderDetail.setGoodsCount(objects1[1] + "");
                orderDetail.setGoodPrice(objects1[2] + "");
            }
            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }
}
