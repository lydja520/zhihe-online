package cn.com.zhihetech.online.controller.v2.api;

import cn.com.zhihetech.online.bean.SystemConfig;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IV2OrderService;
import cn.com.zhihetech.online.vo.OrderDetailInfo;
import cn.com.zhihetech.online.vo.OrderInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pingplusplus.model.Charge;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class OrderApiControllerTest {

    @Resource(name = "v2OrderService")
    protected IV2OrderService v2OrderService;

    @Test
    public void submitOrder() throws Exception {
        List<OrderInfo> orderInfoList = new ArrayList<>();
        //订单1
        OrderInfo orderInfo1 = new OrderInfo();
        orderInfo1.setUserId("21124339-5a96-403d-9821-0879b23771b9");
        orderInfo1.setOrderName("挚合科技测试订单V2_1");
        orderInfo1.setUserMsg("测试");
        orderInfo1.setReceiverName("杨某某");
        orderInfo1.setReceiverPhone("18788520885");
        orderInfo1.setReceiverAdd("关上顺新时代");
        orderInfo1.setPayType("alipay");
        orderInfo1.setCarriage(5);
        orderInfo1.setOrderTotal(75);
        orderInfo1.setSeckillOrder(false);
        orderInfo1.setActivityGodosId(null);
        List<OrderDetailInfo> orderDetailInfos1_1 = new ArrayList<>();
        OrderDetailInfo orderDetailInfo1_1_1 = new OrderDetailInfo();
        orderDetailInfo1_1_1.setGoodsId("15d98782-20b7-447d-be3b-c55686803cc2");
        orderDetailInfo1_1_1.setCount(3);
        orderDetailInfo1_1_1.setPrice(20);
        orderDetailInfo1_1_1.setSkuId("e5bc051a-62c1-4983-aae8-cbb7117ad48f");
        orderDetailInfos1_1.add(orderDetailInfo1_1_1);
        OrderDetailInfo orderDetailInfo1_1_2 = new OrderDetailInfo();
        orderDetailInfo1_1_2.setGoodsId("3c92b6c5-a106-4b97-ae85-6cce1d2968d0");
        orderDetailInfo1_1_2.setCount(2);
        orderDetailInfo1_1_2.setPrice(5);
        orderDetailInfo1_1_2.setSkuId("040e85c9-c1dd-4758-89e8-239953c1b358");
        orderDetailInfos1_1.add(orderDetailInfo1_1_2);
        orderInfo1.setOrderDetails(orderDetailInfos1_1);

        //订单2
        OrderInfo orderInfo2 = new OrderInfo();
        orderInfo2.setUserId("21124339-5a96-403d-9821-0879b23771b9");
        orderInfo2.setOrderName("挚合科技测试订单V2_2");
        orderInfo2.setUserMsg("测试");
        orderInfo2.setReceiverName("杨某某");
        orderInfo2.setReceiverPhone("18788520885");
        orderInfo2.setReceiverAdd("关上顺新时代");
        orderInfo2.setPayType("alipay");
        orderInfo2.setCarriage(0);
        orderInfo2.setOrderTotal(5);
        orderInfo2.setSeckillOrder(false);
        orderInfo2.setActivityGodosId(null);
        List<OrderDetailInfo> orderDetailInfos1_2 = new ArrayList<>();
        OrderDetailInfo orderDetailInfo1_2_1 = new OrderDetailInfo();
        orderDetailInfo1_2_1.setGoodsId("573f719c-3ef4-4dd1-b39f-b1c299ce61d1");
        orderDetailInfo1_2_1.setCount(1);
        orderDetailInfo1_2_1.setPrice(5);
        orderDetailInfo1_2_1.setSkuId("df9b43a2-27b7-46be-96b3-2b09bff2eefe");
        orderDetailInfos1_2.add(orderDetailInfo1_2_1);
        orderInfo2.setOrderDetails(orderDetailInfos1_2);

        orderInfoList.add(orderInfo1);
        orderInfoList.add(orderInfo2);

        String orderInfoListStr = JSON.toJSONString(orderInfoList);

        this.submitOrderTest("21124339-5a96-403d-9821-0879b23771b9", orderInfoListStr);

        System.out.println(orderInfoListStr);
    }

    private void submitOrderTest(String userId, String orderInfoListStr) {
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
        Charge charge = this.v2OrderService.executeSubmitOrderAndGetCharge(userId,orderInfoList, "192.168.1.105");
        System.out.println(charge);
    }

}