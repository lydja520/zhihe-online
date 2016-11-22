package cn.com.zhihetech.online.controller.v2.api;

import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IV2OrderService;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.online.vo.OrderInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Charge;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/7/14.
 */
@Controller("v2OrderApiController")
public class OrderApiController extends V2ApiController {

    @Resource(name = "v2OrderService")
    protected IV2OrderService v2OrderService;


    /*提交普通订单并获取支付Charge对象 post数据格式
     orderInfoListStr：
     json数据格式：
     orderInfoListStr:[{
          orderName: "订单名",
          userId: "用户ID",
          userMsg: "订单留言",
         receiverName: "收件人姓名",
         receiverPhone: "收件人电话号码"，
          receiverAdd: "收件人地址",
          payType: "支付方式,只能为'alipay'或'wx'",
          carriage: "运费",
          orderTotal: "订单总金额（包含运费）",
          seckillOrder: "是否是秒杀商品",
          activityGodosId: "活动商品ID";
          orderDetails:[{
               goodsId: "商品1Id",
               count: "商品1购买数量",
               price: "商品1单价",
               skuId: "商品1对应skuId",
           },{
               goodsId: "商品2Id",
               count: "商品2购买数量",
               price: "商品2单价",
               skuId: "商品2对应skuId",
           }]
      },{
           orderName: "订单名",
                ……
           orderDetails: [{
               ……
           },{
               ……
          }]
      }]
     */

    /**
     * 提交普通订单并获取支付Charge对象
     * url:api/v2/user/{userId}/order/add   userId：当前用户ID,不能为空
     *
     * @param orderInfoListStr
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/order/add", method = RequestMethod.POST)
    public ResponseMessage submitOrder(@PathVariable("userId") String userId, String orderInfoListStr, HttpServletRequest request) {
        List<OrderInfo> orderInfoList = JSONArray.parseArray(orderInfoListStr, OrderInfo.class);
        if (orderInfoList == null || orderInfoList.isEmpty()) {
            throw new SystemException("订单提交失败；错误代码：user_order:add-001");
        }
        for (OrderInfo orderInfo : orderInfoList) {
            orderInfo.setSeckillOrder(false);
            orderInfo.setActivityGodosId(null);
            if (!orderInfo.getPayType().equals(AppConfig.PayType.ALI_PAY) && !orderInfo.getPayType().equals(AppConfig.PayType.WX_PAY)) {
                throw new SystemException("只支持支付宝或微信支付！");
            }
            if (!orderInfo.getUserId().equals(userId)) {
                throw new SystemException("订单提交失败；错误代码：user_order:add-002");
            }
        }
        Charge charge = this.v2OrderService.executeSubmitOrderAndGetCharge(userId, orderInfoList, request.getRemoteAddr());
        ResponseMessage responseMessage = executeResult();
        responseMessage.setAttribute(charge);
        return responseMessage;
    }


    /*
    提交秒杀商品订单并获取支付Charge对象
    url:  api/v2/user/{userId}/secKillOrder/add
    orderInfoStr:{
          orderName: "订单名",
          userId: "用户ID",
          userMsg: "订单留言",
          receiverName: "收件人姓名",
          receiverPhone: "收件人电话号码"，
          receiverAdd: "收件人地址",
          payType: "支付方式,只能为'alipay'或'wx'",
          carriage: "运费",
          orderTotal: "订单总金额（包含运费）",
          seckillOrder: "是否是秒杀商品",
          activityGodosId: "活动商品ID";
          orderDetails:{
               goodsId: "商品Id",
               count: "商品购买数量",
               price: "商品单价",
               skuId: "商品对应skuId",
           }
      }
     */

    /**
     * 提交秒杀商品订单并获取支付Charge对象
     * url:  api/v2/user/{userId}/secKillOrder/add
     *
     * @param userId
     * @param orderInfoStr
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/secKillOrder/add", method = RequestMethod.POST)
    public ResponseMessage submitSecKillOrder(@PathVariable("userId") String userId, String orderInfoStr, HttpServletRequest request) {
        OrderInfo orderInfo = JSONObject.parseObject(orderInfoStr, OrderInfo.class);
        if (orderInfo == null) {
            throw new SystemException("订单提交失败；错误代码：user_order:add-001");
        }
        orderInfo.setSeckillOrder(true);
        if (StringUtils.isEmpty(orderInfo.getActivityGodosId())) {
            throw new SystemException("不支持此操作！错误代码：seckill_order:add-001");
        }
        if (!orderInfo.getPayType().equals(AppConfig.PayType.ALI_PAY) && !orderInfo.getPayType().equals(AppConfig.PayType.WX_PAY)) {
            throw new SystemException("只支持支付宝或微信支付！");
        }
        Charge charge = this.v2OrderService.executeSubmitSeckillOrderAndGetCharge(orderInfo, request.getRemoteAddr());
        ResponseMessage responseMessage = executeResult();
        responseMessage.setAttribute(charge);
        return responseMessage;
    }

    /**
     * 用户提交订单后，取消支付，在订单管理里重新支付
     * url: api/v2/user/{userId}/order/{orderId}/pay
     * <p>
     *
     * @param userId
     * @param orderId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/order/{orderId}/pay")
    public ResponseMessage againPayOrder(@PathVariable(value = "userId") String userId, @PathVariable(value = "orderId") String orderId, HttpServletRequest request) {
        if (cn.com.zhihetech.util.common.StringUtils.isEmpty(userId) || cn.com.zhihetech.util.common.StringUtils.isEmpty(orderId)) {
            throw new SystemException("用户id或订单Id");
        }
        Charge charge = this.v2OrderService.executeOrderAgainPay(userId, orderId, request.getRemoteAddr());
        ResponseMessage responseMessage = executeResult();
        responseMessage.setAttribute(charge);
        return responseMessage;
    }

}
