package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.bean.ChargeInfo;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.vo.OrderInfo;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.model.Charge;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/7/14.
 */
public class PingppUtils {

    /**
     * pingpp 管理平台对应的 API key
     */
    private static String apiKey = AppConfig.PingppConfig.PINGPP_API_KEY;

    /**
     * pingpp 管理平台对应的应用 ID
     */
    private static String appId = AppConfig.PingppConfig.PINGPP_API_ID;

    /**
     * 根据chargeInfo获取支付对象charge
     * @param chargeInfo
     * @return
     */
    public static Charge getCharge(ChargeInfo chargeInfo) {
        Charge charge = null;
        Pingpp.apiKey = PingppUtils.apiKey;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", PingppUtils.appId);
        chargeMap.put("app", app);
        chargeMap.put("amount", chargeInfo.getAmount());
        chargeMap.put("currency", chargeInfo.getCurrency());
        chargeMap.put("subject", chargeInfo.getSubject());
        chargeMap.put("body", chargeInfo.getBody());
        chargeMap.put("order_no", chargeInfo.getOrderNo());
        chargeMap.put("channel", chargeInfo.getChannel());
        chargeMap.put("client_ip", chargeInfo.getClientIp());
        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
        } catch (PingppException e) {
            throw new SystemException("调用支付控件失败，请重试！", e);
        }
        if (charge == null) {
            throw new SystemException("调用支付控件失败，请重试！");
        } else {
            if (charge.getPaid()) {
                throw new SystemException("此订单已经成功支付，请耐心等待支付结果！");
            }
            if (charge.getRefunded()) {
                throw new SystemException("该订单已经申请过退款，不支持支付操作");
            }
        }
        return charge;
    }

}
