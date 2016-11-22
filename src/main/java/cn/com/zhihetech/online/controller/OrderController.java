package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.components.ConfirmReceiveOrderSchedule;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.IOrderDetailService;
import cn.com.zhihetech.online.service.IOrderService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.ExportExcelUtil;
import cn.com.zhihetech.online.util.PropertiesUtils;
import cn.com.zhihetech.online.vo.ExportOrder;
import cn.com.zhihetech.online.vo.OrderSearch;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.quartz.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.Calendar;

/**
 * Created by YangDaiChun on 2016/1/5.
 */
@Controller
public class OrderController extends SupportController {

    @Resource(name = "orderService")
    private IOrderService orderService;

    @Resource(name = "orderDeatilService")
    private IOrderDetailService orderDetailService;

    @Resource(name = "adminService")
    private IAdminService adminService;

    /**
     * 订单页-商家
     *
     * @return
     */
    @RequestMapping(value = "/admin/order")
    public String indexPage() {
        return "admin/order/order";
    }

    /**
     * <h3>订单列表</h3>
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/order/list")
    public PageData<Order> getAllOrderPageData(HttpServletRequest request, OrderSearch orderSearch) {
        IQueryParams queryParams = this.createQueryParams(request);
        String merchantId = this.getCurrentMerchatId(request);
        if (!StringUtils.isEmpty(merchantId)) {
            queryParams.andEqual("merchant.merchantId", merchantId);
        }
        return this.orderService.getSearchPageData(request, queryParams, this.createPager(request), orderSearch);
    }

    /**
     * 订单页-管理员
     *
     * @return
     */
    @RequestMapping(value = "admin/api/ordersForAdmin")
    public String orderForAdminIndex() {
        return "admin/order/ordersForAdmin";
    }

    /**
     * 下载订单数据
     *
     * @param request
     * @param response
     * @param orderSearch
     * @throws IOException
     */
    @RequestMapping(value = "admin/api/order/export")
    public void exportOrderToExcel(HttpServletRequest request, HttpServletResponse response, OrderSearch orderSearch) throws IOException {
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + System.currentTimeMillis() + ".xls");
        if (!StringUtils.isEmpty(orderSearch.getSearchStartTime())) {
            orderSearch.setSearchStartTime(orderSearch.getSearchStartTime() + " 00:00:00");
        }
        if (!StringUtils.isEmpty(orderSearch.getSearchEndTime())) {
            orderSearch.setSearchEndTime(orderSearch.getSearchEndTime() + " 23:59:59");
        }
        ExportExcelUtil<ExportOrder> exp = new ExportExcelUtil<>();
        String[] headers = {"订单创建时间", "订单号", "订单状态", "商家名", "商家电话", "商家联系人电话", "用户账号", "收件人姓名",
                "收件人电话号码", "收件人地址", "购买的商品名", "商品属性","购买的商品数量"};
        String merchantId = this.getCurrentMerchatId(request);
        List<ExportOrder> exportOrders = this.orderService.getExportOrders(merchantId, orderSearch);
        String sheetName = null;
        if (StringUtils.isEmpty(this.getCurrentMerchatId(request))) {
            sheetName = "实淘订单";
        } else {
            sheetName = exportOrders.get(0).getMerchantName();
        }
        OutputStream out = response.getOutputStream();
        HSSFWorkbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook();
            exp.exportExcel(hssfWorkbook, sheetName, headers, exportOrders);
            hssfWorkbook.write(out);
            out.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (hssfWorkbook != null) {
                hssfWorkbook.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 显示订单的详细信息
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "admin/order/{orderId}/orderDetailsInfo")
    public ModelAndView getOrderDetailsInfo(@PathVariable(value = "orderId") String orderId) {
        ModelAndView modelAndView = new ModelAndView("admin/order/orderDetailsInfo");
        Order order = this.orderService.getById(orderId);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("order.orderId", orderId);
        List<OrderDetail> orderDetails = this.orderDetailService.getAllByParams(null, queryParams);
        modelAndView.addObject("order", order);
        modelAndView.addObject("orderDetails", orderDetails);
        return modelAndView;
    }


    /*======================================待付款======================================*/
    @RequestMapping(value = "admin/order/waitPay")
    public String waitPayIndexPage() {
        return "admin/order/waitPay";
    }

    /**
     * 修改订单总价
     *
     * @param orderId
     * @param orderTotal
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/order/updateOrderTotal", method = RequestMethod.POST)
    public ResponseMessage updateOrderTotal(String orderId, Float orderTotal) {
        if (orderTotal <= 0) {
            throw new SystemException("总价不能小于或等于0！");
        }
        this.orderService.updateAndAddEditRecord(orderId, orderTotal);
        return executeResult();
    }

     /*======================================订单等待发货======================================*/

    @RequestMapping(value = "admin/order/waitDispatcher")
    public String waitDispatcher() {
        return "admin/order/waitDispatcher";
    }

    /**
     * <h3>修改订单收货信息</h3>
     *
     * @param orderId
     * @param receiverName
     * @param receiverPhone
     * @param receiverAdd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/order/updateReceiverInfo")
    public ResponseMessage updateReceiverInfo(String orderId, String receiverName, String receiverPhone, String receiverAdd) {
        Order order = this.orderService.getById(orderId);
        if (order == null) {
            throw new SystemException("不存在该订单！");
        }
        if (order.getOrderState() == Constant.ORDER_STATE_NO_PAYMENT || order.getOrderState() == Constant.ORDER_STATE_NO_DISPATCHER) {
            order.setReceiverName(receiverName);
            order.setReceiverPhone(receiverPhone);
            order.setReceiverAdd(receiverAdd);
        } else {
            throw new SystemException("该订单不支持修改收货信息操作，因为订单状态已经发生改变，请刷新页面后再试！");
        }
        this.orderService.update(order);
        return executeResult();
    }

    /**
     * <h3>订单发货操作</h3>
     *
     * @param orderId
     * @param carriageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/order/updateDispatcherState")
    public ResponseMessage updateDispatcherState(String orderId, String carriageNum) {
        this.orderService.executeDispacher(orderId, carriageNum);
        return executeResult();
    }

    /*======================================订单退款======================================*/
    @RequestMapping(value = "admin/order/refund")
    public String refundIndexPage() {
        return "admin/order/orderRefund";
    }

    @RequestMapping(value = "admin/order/refundOk")
    public String refundOkIndexPage() {
        return "admin/order/orderRefundOk";
    }


    /**
     * <h3>订单确认退款操作</h3>
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/order/refund")
    public ResponseMessage Refund(String orderId) {
        return this.orderService.executeConfirmRefund(orderId);
    }

    /*=====================================订单等待收货======================================*/
    @RequestMapping(value = "admin/order/waitDeliver")
    public String waitDeliverIndexPage() {
        return "admin/order/waitDeliver";
    }


    /*======================================订单已签收和已评价======================================*/
    @RequestMapping(value = "admin/order/alreadyFinish")
    public String alreadyDeliver() {
        return "admin/order/alreadyFinish";
    }

    /**
     * 等待平台退款的订单
     *
     * @return
     */
    @RequestMapping("admin/orders/waitPlatRefund")
    public String waitPlatRefundPageIndex() {
        return "admin/order/waitPlatRefundOrders";
    }

    /**
     * 分页获取需要等待平台退款的订单（目前只限支付宝支付的订单）
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("admin/api/waiplatrefund/orders")
    public PageData<Order> waitPlatRefundOrders(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request);
        return this.orderService.getWaitPlatRefundOrders(createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping("admin/api/waitPlatRefund/order/refund")
    public ResponseMessage executeAlipayRefunded(String orderId, String alipayRefundTransacCode) {
        this.orderService.executeAlipayRefunded(orderId, alipayRefundTransacCode);
        return executeResult();
    }
}
