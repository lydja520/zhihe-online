package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityOrderService;
import cn.com.zhihetech.online.util.alipay.AlipayNotify;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by YangDaiChun on 2016/5/26.
 */
@Controller
public class AlipayDirectPayCallBackController extends SupportController {

    @Resource(name = "activityOrderService")
    private IActivityOrderService activityOrderService;

    /**
     * 支付成功，同步页面跳转回调
     *
     * @param request
     */
    @RequestMapping(value = "alipay/directPayReturnUrl", method = RequestMethod.GET)
    public ModelAndView returnUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView("admin/activityOrder/aliReturnInfo");
        /**********获取支付宝GET过来反馈信息**********/
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        /**********获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表**********/
        //商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
        //卖家账号
        String seller_email = new String(request.getParameter("seller_email").getBytes("ISO-8859-1"), "UTF-8");
        //买家账号
        String buyer_email = new String(request.getParameter("buyer_email").getBytes("ISO-8859-1"), "UTF-8");
        //交易金额
        String total_fee = new String(request.getParameter("total_fee").getBytes("ISO-8859-1"), "UTF-8");

        /**********计算得出通知验证结果**********/
        boolean verify_result = AlipayNotify.verify(params);
        if (verify_result) {//验证成功

            if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行下面的业务程序
                //如果有做过处理，不执行下面业务程序
                try {
                    this.activityOrderService.executePayResult(out_trade_no, trade_no, seller_email, buyer_email, total_fee);
                    mv.addObject("info", "实淘提示，恭喜你！该活动提交成功，请回实淘管理系统，刷新进行确认！");
                } catch (SystemException e) {
                    mv.addObject("info", e.getMsg());
                }
            }

        } else {
            //验证失败
            mv.addObject("info", "对不起！，验证失败，请按正确的按照流程进行支付！");
        }
        return mv;
    }

    /**
     * 支付成功，异步回调，客服端服务器以此回调为准
     */
    @RequestMapping(value = "alipay/directPayNotifyUrl")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**********获取支付宝POST过来反馈信息**********/
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        /**********获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)**********/
        //商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
        //卖家账号
        String seller_email = new String(request.getParameter("seller_email").getBytes("ISO-8859-1"), "UTF-8");
        //买家账号
        String buyer_email = new String(request.getParameter("buyer_email").getBytes("ISO-8859-1"), "UTF-8");
        //交易金额
        String total_fee = new String(request.getParameter("total_fee").getBytes("ISO-8859-1"), "UTF-8");

        /**********计算得出验证结果**********/
        boolean verify_result = AlipayNotify.verify(params);
        if (verify_result) {//验证成功

            if (trade_status.equals("TRADE_SUCCESS") || trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序
                try {
                    this.activityOrderService.executePayResultConfirm(out_trade_no, trade_no, seller_email, buyer_email, total_fee);
                } catch (SystemException e) {
                    response.getWriter().print("fail");
                    return;
                }
                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }
            response.getWriter().print("success");    //验证成功，向阿里服务器，发送成功信息
        } else {//验证失败,向阿里服务器，发送失败信息
            response.getWriter().print("fail");
        }
    }
}
