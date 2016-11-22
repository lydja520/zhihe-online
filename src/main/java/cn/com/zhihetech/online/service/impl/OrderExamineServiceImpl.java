package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.bean.OrderExamine;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.dao.IMerchantBillDetailDao;
import cn.com.zhihetech.online.dao.IOrderDao;
import cn.com.zhihetech.online.dao.IOrderExamineDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.AbstractService;
import cn.com.zhihetech.online.service.IOrderExamineService;
import cn.com.zhihetech.online.util.NumberUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ShenYunjie on 2016/6/29.
 */
@Service("orderExamineService")
public class OrderExamineServiceImpl extends AbstractService<OrderExamine> implements IOrderExamineService {

    @Resource(name = "orderExamineDao")
    private IOrderExamineDao orderExamineDao;
    @Resource(name = "merchantBillDetailDao")
    private IMerchantBillDetailDao merchantBillDetailDao;
    @Resource(name = "orderDao")
    private IOrderDao orderDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public OrderExamine getById(String id) {
        return this.orderExamineDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param orderExamine 需要删除的持久化对象
     */
    @Override
    public void delete(OrderExamine orderExamine) {
        this.orderExamineDao.deleteEntity(orderExamine);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param orderExamine 需要持久化的对象
     * @return
     */
    @Override
    public OrderExamine add(OrderExamine orderExamine) {
        return this.orderExamineDao.saveEntity(orderExamine);
    }

    /**
     * 更新一个持久化对象
     *
     * @param orderExamine
     */
    @Override
    public void update(OrderExamine orderExamine) {
        this.orderExamineDao.updateEntity(orderExamine);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<OrderExamine> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.orderExamineDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<OrderExamine> getPageData(Pager pager, IQueryParams queryParams) {
        return this.orderExamineDao.getPageData(pager, queryParams);
    }

    /**
     * 生成对账单数据
     *
     * @param workbook  微信（支付宝）账单文件
     * @param batchCode 平台账单批次
     * @param payType   支付方式（微信支付或支付宝支付）
     */
    @Override
    public void executeCreateExamineBill(Workbook workbook, String batchCode, String payType) {
        List<OrderExamine> orderExamines = createOrderExamine(workbook, getPayOrderCodesByBillBatchCodeAndPayType(batchCode, payType), payType);
        if (orderExamines == null && orderExamines.isEmpty()) {
            return;
        }

        this.deleteByBatchCodeAndPayType(batchCode, payType);
        for (OrderExamine orderExamine : orderExamines) {
            orderExamine.setBatchCode(batchCode);
            this.add(orderExamine);
        }
    }

    /**
     * 根据账单批次和支付方式删除对账数据
     *
     * @param batchCode 账单批次
     * @param payType   支付方式
     */
    private void deleteByBatchCodeAndPayType(String batchCode, String payType) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("batchCode", batchCode)
                .andEqual("payType", payType);
        this.orderExamineDao.executeDelete(queryParams);
    }

    private List<OrderExamine> createOrderExamine(Workbook workbook, List<String> payOrderCodes, String payType) {
        List<OrderExamine> orderExamines = new ArrayList<>();
        IQueryParams queryParams = null;
        for (String payCode : payOrderCodes) {
            OrderExamine orderExamine = new OrderExamine();
            orderExamine.setPayOrderCode(payCode);
            queryParams = new GeneralQueryParams().andEqual("pingPPorderNo", payCode);
            List<Order> orders = this.orderDao.getEntities(null, queryParams);
            if (orders == null || orders.isEmpty()) {
                orderExamine.setExamineDate(new Date());
                orderExamine.setExamineOk(false);
                orderExamine.setBatchOrder(false);
                orderExamine.setExamineMsg("未找到支付订单为\"" + payCode + "\"对应的平台订单！");
                orderExamines.add(orderExamine);
                orderExamine.setPayType(payType);
                continue;
            }
            orderExamine = createOrderExamineByPayType(workbook, orders, payCode, payType);
            orderExamines.add(orderExamine);
        }
        return orderExamines;
    }

    private OrderExamine createOrderExamineByPayType(Workbook workbook, List<Order> orders, String payCode, String payType) {
        OrderExamine orderExamine = new OrderExamine();
        String orderCodes = new String();
        String orderIds = new String();
        double orderAmount = 0d;
        for (Order order : orders) {
            orderCodes += order.getOrderCode() + ",";
            orderIds += order.getOrderId() + ",";
            orderAmount += order.getOrderTotal();
        }
        orderExamine.setOrderCode(orderCodes);
        orderExamine.setOrderId(orderIds);
        orderExamine.setOrderAmount(NumberUtils.doubleScale(2, orderAmount));

        orderExamine.setPayType(payType);
        orderExamine.setExamineDate(new Date());
        orderExamine.setPayOrderCode(payCode);
        orderExamine.setBatchOrder(orders.size() > 1);
        double payAmount = 0d;
        switch (payType) {
            case AppConfig.PayType.ALI_PAY:
                payAmount = getAliPayAmount(workbook, payCode);
                break;
            case AppConfig.PayType.WX_PAY:
                payAmount = getWXPayAmount(workbook, payCode);
                break;
        }
        orderExamine.setPayAmount(payAmount);
        orderExamine.setExamineOk(orderExamine.getOrderAmount() == payAmount);
        return orderExamine;
    }

    /**
     * 根据账单批次获取账单支付订单号
     *
     * @param batchCode
     * @return
     */
    private List<String> getPayOrderCodesByBillBatchCodeAndPayType(String batchCode, String payType) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("merchantBill.batchCode", batchCode)
                .andEqual("order.payType", payType);
        List<Object> payOrderCodes = this.merchantBillDetailDao.getProperty("order.pingPPorderNo", null, queryParams);
        if (payOrderCodes == null || payOrderCodes.isEmpty()) {
            throw new SystemException("order is empty or error with the batchcode!");
        }
        List<String> payCodes = new ArrayList<>();
        for (Object obj : payOrderCodes) {
            if (payCodes.contains(obj)) {
                continue;
            }
            payCodes.add(obj.toString());
        }
        return payCodes;
    }

    /**
     * 生成微信对账单
     *
     * @param workbook 支付账单文件
     * @param payCode  支付订单号
     */
    private double getWXPayAmount(Workbook workbook, String payCode) {
        int payCodeIndex = 2;   //支付订单号列索引
        int payAmountIndex = 11; //支付金额列索引
        int startIndex = 5; //开始读取行数

        Sheet sheet = workbook.getSheetAt(0);
        int endIndex = sheet.getLastRowNum();
        int currentRowIndex = startIndex;

        while (currentRowIndex <= endIndex) {
            Cell codeCell = getCellByRowIndexAndColIndex(sheet, currentRowIndex, payCodeIndex);
            String code = codeCell.getStringCellValue();
            code = code.substring(1, code.length());
            if (code.equals(payCode)) {
                Cell priceCell = getCellByRowIndexAndColIndex(sheet, currentRowIndex, payAmountIndex);
                Double payAmount = NumberUtils.doubleScale(2, priceCell.getNumericCellValue());
                return payAmount;
            }
            currentRowIndex++;
        }
        return 0d;
    }

    /**
     * 生成支付宝对账单
     *
     * @param workbook 支付账单文件
     * @param payCode  支付订单号
     */
    private double getAliPayAmount(Workbook workbook, String payCode) {
        int payCodeIndex = 1;   //支付订单号列索引
        int payAmountIndex = 9; //支付金额列索引
        int startIndex = 5; //开始读取行数

        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum() + 1;
        int endIndex = rowCount - 7 - 1;
        int currentRowIndex = startIndex;

        while (currentRowIndex <= endIndex) {
            Cell codeCell = getCellByRowIndexAndColIndex(sheet, currentRowIndex, payCodeIndex);
            String code = codeCell.getStringCellValue().trim();
            if (code.equals(payCode)) {
                Cell priceCell = getCellByRowIndexAndColIndex(sheet, currentRowIndex, payAmountIndex);
                Double payAmount = NumberUtils.doubleScale(2, priceCell.getNumericCellValue());
                return payAmount;
            }
            currentRowIndex++;
        }
        return 0d;
    }

    /**
     * 获取指定行和列的Cell
     *
     * @param sheet     需要操作的sheet
     * @param rowIndex  行索引
     * @param cellIndex 列索引
     * @return
     */
    private Cell getCellByRowIndexAndColIndex(Sheet sheet, int rowIndex, int cellIndex) {
        return sheet.getRow(rowIndex).getCell(cellIndex);
    }
}
