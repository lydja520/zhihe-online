package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.vo.OrderInfo;
import com.pingplusplus.model.Charge;

import java.util.List;

/**
 * Created by YangDaiChun on 2016/7/14.
 */
public interface IV2OrderService {


    /**
     * 提交普通商品订单并获取charge对象
     *
     * @param orderInfos
     * @param clientIp
     * @return
     */
    Charge executeSubmitOrderAndGetCharge(String userId,List<OrderInfo> orderInfos, String clientIp);

    /**
     * 提交秒杀商品订单并获取charge对象
     *
     * @param orderInfo
     * @param clientIp
     * @return
     */
    Charge executeSubmitSeckillOrderAndGetCharge(OrderInfo orderInfo, String clientIp);

    Charge executeOrderAgainPay(String userId, String orderId, String remoteAddr);
}
