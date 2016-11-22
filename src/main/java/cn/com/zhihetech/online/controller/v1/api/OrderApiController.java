package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IOrderDetailService;
import cn.com.zhihetech.online.service.IOrderService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.vo.OrderEvaluateTemp;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.SubQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Charge;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by YangDaiChun on 2016/1/4.
 */
@Controller
public class OrderApiController extends ApiController {

    @Resource(name = "orderService")
    private IOrderService orderService;

    @Resource(name = "orderDeatilService")
    private IOrderDetailService orderDetailService;

    /**
     * <h3>普通商品订单支付</h3>
     * <p>URL: api/order/add</p>
     * <p>
     * 参数:<br>
     * orderStr:   <br>
     * <p>
     * json数据格式：
     * orderStr：[
     * {
     * orderName: 订单名 <br>
     * user.userId: 用户ID <br>
     * userMsg:给商家留言 <br>
     * receiverName:签收人名<br>
     * receiverPhone:签收人电话号码<br>
     * receiverAdd:签收人地址<br>
     * payType: 传参数  "alipay" 或  "wx"  <br>
     * carriage:订单运费  <br>
     * orderTotal:订单总金额,不包含运费<br>
     * orderDetailInfo:组成格式 <br>
     * },{
     * orderName: 订单名 <br>
     * user.userId: 用户ID <br>
     * userMsg:给商家留言 <br>
     * receiverName:签收人名<br>
     * receiverPhone:签收人电话号码<br>
     * receiverAdd:签收人地址<br>
     * payType: 传参数  "alipay" 或  "wx"  <br>
     * carriage:订单运费  <br>
     * orderTotal:订单总金额,包含运费<br>
     * orderDetailInfo:组成格式 <br>
     * },……
     * ]
     * orderDetailInfo:组成格式 <br>
     * 例如：商品1id*商品1数量#商品1单价&商品2id*商品2数量#商品2单价&商品3Id…………<br>
     */
    @ResponseBody
    @RequestMapping(value = "order/add", method = RequestMethod.POST)
    public ResponseMessage addOrder(String orderStr, HttpServletRequest request) {
/*        throw new SystemException("尊敬的用户，为了提升用户体验，我们已经对APP进行了升级，此版本暂不支持购买，请更新到最新版本！");*/
        List<Order> orders = JSONArray.parseArray(orderStr, Order.class);
        ResponseMessage responseMessage = this.orderService.addOrderAndCharge(orders, request);
        if (responseMessage.getAttribute() != null) {
            return responseMessage;
        }
        return executeResult(ResponseStatusCode.SYSTEM_ERROR_CODE, "订单提交失败！");
    }

    /**
     * <h3>活动商品订单支付</h3>
     * <p>URL: api/activityGoodsOrder/add</p>
     * <p>参数</p>
     * orderName: 订单名 <br>
     * user.userId: 用户ID <br>
     * userMsg:给商家留言 <br>
     * receiverName:签收人名<br>
     * receiverPhone:签收人电话号码<br>
     * receiverAdd:签收人地址<br>
     * payType: 传参数  "alipay" 或  "wx"  <br>
     * carriage:订单运费  <br>
     * orderTotal:订单总金额,包含运费<br>
     * activityGoods.agId :活动商品Id  <br>
     */
    @ResponseBody
    @RequestMapping(value = "activityGoodsOrder/add", method = RequestMethod.POST)
    public ResponseMessage addActivityGoodsOrder(Order order, HttpServletRequest request) {
        Charge charge = this.orderService.addActivityGoodsOrder(order, request);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "订单提交成功", charge);
    }

    /**
     * <h3>用户提交订单成功后并取消付款，重新付款</h3>
     * url:  api/order/{orderId}/pay  <br>
     * {orderId}:订单Id  <br>
     */
    @ResponseBody
    @RequestMapping(value = "order/{orderId}/pay")
    public ResponseMessage orderPay(@PathVariable(value = "orderId") String orderId, HttpServletRequest request) {
        Charge charge = this.orderService.executeOrderPay(orderId, request);
        if (charge != null) {
            return executeResult(ResponseStatusCode.SUCCESS_CODE, "charge获取成功", charge);
        }
        return executeResult(ResponseStatusCode.SYSTEM_ERROR_CODE, "charge获取失败");
    }

    /**
     * <h3>取消订单（买家手动取消，包括普通商品订单或者活动商品订单）</h3>
     * url :  api/order/{orderId}/cancel  <br>
     * {orderId}:订单Id  <br>
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "order/{orderId}/cancel")
    public ResponseMessage cancelOrder(@PathVariable(value = "orderId") String orderId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("orderId", orderId);
        List<Object> orderStates = this.orderService.getProperty("orderState", null, queryParams);
        if (orderStates.size() <= 0) {
            throw new SystemException("找不到对应的订单");
        }
        int orderState = (int) orderStates.get(0);
        if (orderState != Constant.ORDER_STATE_NO_PAYMENT) {
            throw new SystemException("当前订单状态不支持取消操作，请刷新后再试！");
        }
        this.orderService.executeCancelOrder(orderId);
        return executeResult();
    }

    /**
     * <h3>确认收货</h3>
     * url : api/order/{orderId}/confirmReceipt   <br>
     * {orderId}: 订单Id
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "order/{orderId}/confirmReceipt")
    public ResponseMessage confirmReceipt(@PathVariable(value = "orderId") String orderId) {
        this.orderService.updateOrderAndAddBillDetail(orderId);
        return executeResult();
    }

    /**
     * <h3>根据用户Id获取该用户的订单 </h3>
     * URL: api/order/list <br>
     * <p>
     * <h4>参数</h4>
     * userId : 用户Id  <br>
     * orderState  :  订单状态 ,不传参数为获取所有订单  <br>
     * page: 第几页  <br>
     * rows:每页多少条  <br>
     * 不传参数默认为获取第一页的20条数据  <br>
     *
     * @param userId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/order/list")
    public PageData<Order> getOrders(String userId, @RequestParam(name = "orderState", defaultValue = "0") int orderState, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        if (orderState == 0) {
            queryParams.andEqual("user.userId", userId).andEqual("deleteState", false).sort("createDate", cn.com.zhihetech.util.hibernate.Order.DESC);
        } else if (orderState == 101) {
            SubQueryParams subQueryParams = new SubQueryParams();
            subQueryParams.andEqual("orderState", Constant.ORDER_STATE_WAIT_REFUND).orEqual("orderState", Constant.ORDER_STATE_ALREADY_REFUND);
            queryParams.andEqual("user.userId", userId).andEqual("deleteState", false).sort("createDate", cn.com.zhihetech.util.hibernate.Order.DESC).andSubParams(subQueryParams);
        } else {
            queryParams.andEqual("user.userId", userId).andEqual("orderState", orderState).andEqual("deleteState", false).sort("createDate", cn.com.zhihetech.util.hibernate.Order.DESC);
        }
        return this.orderService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * 根据订单Id,查询该订单的基本信息 <br>
     * URL:api/order/{orderId} <br>
     * {orderId}:订单id
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "order/{orderId}")
    public Order getOrder(@PathVariable(value = "orderId") String orderId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("orderId", orderId);
        return this.orderService.getById(orderId);
    }

    /**
     * 根据订单Id,查询该订单的详细信息 <br>
     * <p>
     * URL：api/order/{orderId} /orderDetails<br>
     * <p>
     * {orderId}:订单id  <br>
     *
     * @param orderId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "order/{orderId} /orderDetails")
    public List<OrderDetail> getOrderDetails(@PathVariable(value = "orderId") String orderId, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("order.orderId", orderId);
        return this.orderDetailService.getAllByParams(this.createPager(request), queryParams);
    }


    /**
     * <h3>订单申请退款</h3>
     * url :  api/order/{orderId}/refund <br>
     * {orderId}:订单Id  <br>
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "order/{orderId}/refund")
    public ResponseMessage refund(@PathVariable(value = "orderId") String orderId) {
        return this.orderService.updateRefund(orderId);
    }

    /**
     * <h3>订单评价</h3>
     * <p>URL: api/order/evaluate</p>
     * <p>
     * 参数:<br>
     * orderEvaluateStr:   <br>
     * <p>
     * json数据格式：
     * {
     * "orderId": 订单ID,
     * “ score”：商家评分,
     * "goodsScores":[
     * {
     * "goodsId": 订单中的商品1Id,
     * "score": 订单中的商品1评分,
     * "evaluate":  订单中的商品1评价
     * },
     * {
     * "goodsId": 订单中的商品2Id,
     * "score": 订单中的商品2评分,
     * "evaluate":  订单中的商品2评价
     * }
     * ]
     * }
     */
    @ResponseBody
    @RequestMapping(value = "order/evaluate", method = RequestMethod.POST)
    public ResponseMessage evaluate(String orderEvaluateStr) {
        OrderEvaluateTemp orderEvaluateTemp = JSONObject.parseObject(orderEvaluateStr, OrderEvaluateTemp.class);
        this.orderService.updateEvaluateMsg(orderEvaluateTemp);
        return executeResult();
    }

    /**
     * <h3>删除订单</h3>
     * url:  api/order/{orderId}/delete<br>
     * {orderId}:订单Id <br>
     */
    @ResponseBody
    @RequestMapping(value = "order/{orderId}/delete")
    public ResponseMessage deletOrder(@PathVariable(value = "orderId") String orderId) {
        return this.orderService.deleteOrder(orderId);
    }

    /**
     * 当客户端支付成功后回调此接口通知服务器客服端支付成功，防止用户重复支付
     *
     * @param chargeOrderNO 返回给用户端的charge对应的order_no属性
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "order/{chargeOrderNO}/clientPaid", method = RequestMethod.POST)
    public ResponseMessage executeClientPaySuccess(@PathVariable("chargeOrderNO") String chargeOrderNO) {
        this.orderService.executeClientPaid(chargeOrderNO);
        return executeResult();
    }
}