package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.bean.ActivityOrder;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityOrderService;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.vo.ActivityOrderAndActivityOrderDeatils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
@Controller
public class ActivityOrderController extends SupportController {
    @Resource(name = "activityOrderService")
    private IActivityOrderService activityOrderService;
    @Resource(name = "activityService")
    private IActivityService activityService;

    @RequestMapping(value = "admin/activityOrder")
    public String activityOrderPage(HttpServletRequest request) {
        if (this.getCurrentMerchatId(request) != null) {
            return "admin/merchantActivityOrder";
        }
        return "admin/activityOrder";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activityOrder/list")
    public PageData<ActivityOrder> getActivityOrderInfo(HttpServletRequest request, String searchActivityOrderCode, String searchUserpayCode, String initTime, String endTime) {
        IQueryParams queryParams = createQueryParams(request);
        String merchantId = this.getCurrentMerchatId(request);
        queryParams.andEqual("payState", true);
        //判断是否是商家账号
        if (!StringUtils.isEmpty(merchantId)) {
            queryParams.andEqual("merchant.merchantId", merchantId);
        }

        if (!cn.com.zhihetech.online.util.StringUtils.isEmpty(searchActivityOrderCode)) {
            queryParams.andAllLike("orderCode", searchActivityOrderCode);
        }
        if (!cn.com.zhihetech.online.util.StringUtils.isEmpty(searchUserpayCode)) {
            queryParams.andAllLike("buyerAccount", searchUserpayCode);
        }
        if (!StringUtils.isEmpty(initTime)) {
            queryParams.andMoreAndEq("payDate", DateUtils.getStartDateTimeWithDate(DateUtils.String2Date(initTime)));
        }
        if (!StringUtils.isEmpty(endTime)) {
            queryParams.andLessAndEq("payDate", DateUtils.getEndDateTimeWithDate(DateUtils.String2Date(endTime)));
        }
        PageData<ActivityOrder> activityOrderPageData = this.activityOrderService.getPageData(createPager(request), queryParams);
        return activityOrderPageData;
    }

    /**
     * 生成和查询活动订单
     *
     * @param activityId
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "admin/api/activity/{activityId}/payOrder")
    public ModelAndView payActivityOrder(@PathVariable(value = "activityId") String activityId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView("admin/activityOrder/activityOrderSubmit");
        Activity activity = this.activityService.getById(activityId);
        if (activity == null) {
            response.getWriter().print("找不到对应的活动！");
            return null;
        }
        if (activity.getCurrentState() != Constant.ACTIVITY_STATE_UNSBUMIT) {
            response.getWriter().print("当前活动状态不支持支付订单操作!,请刷新确认当前活动状态！");
            return null;
        }
        String merchantId = this.getCurrentMerchatId(request);
        ActivityOrderAndActivityOrderDeatils activityOrderAndActivityOrderDeatils = null;
        String sessionActivityOrderId = (String) request.getSession().getAttribute("activityOrderId");
        if (sessionActivityOrderId != null) {
            String activityOrderId = (String) request.getSession().getAttribute("activityOrderId");
            ActivityOrder activityOrder = this.activityOrderService.getById(activityOrderId);
            if (activityOrder.isPayState()) {
                try {
                    activityOrderAndActivityOrderDeatils = this.activityOrderService.executeQueryOrder(merchantId, activityId);
                    if (activityOrderAndActivityOrderDeatils == null) {
                        response.getWriter().println("恭喜你，提交成功，此次你没有需要支付红包费用和活动费用！");
                        return null;
                    }
                } catch (SystemException e) {
                    response.getWriter().println(e.getMsg());
                    return null;
                }
            } else {
                try {
                    activityOrderAndActivityOrderDeatils = this.activityOrderService.executeQueryOrderByOrderId(activityOrderId);
                } catch (SystemException e) {
                    response.getWriter().println(e.getMsg());
                    return null;
                }
                mv.addObject("info", "之前订单还未支付完成,点击'去支付'对原来的订单进行支付!");
            }
        } else {
            try {
                activityOrderAndActivityOrderDeatils = this.activityOrderService.executeQueryOrder(merchantId, activityId);
                if (activityOrderAndActivityOrderDeatils == null) {
                    response.getWriter().println("提交成功!");
                    return null;
                }
            } catch (SystemException e) {
                response.getWriter().println(e.getMsg());
                return null;
            }
        }
        mv.addObject("activityOrderAndActivityOrderDeatils", activityOrderAndActivityOrderDeatils);
        return mv;
    }

    /**
     * 跳到活动订单支付页面
     *
     * @param request
     * @param response
     * @param activitOrderId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "admin/api/activityOrder/{activitOrderId}/pay")
    public ModelAndView commintMerchAlliance(HttpServletRequest request, HttpServletResponse response, @PathVariable("activitOrderId") String activitOrderId) throws IOException {
        if (StringUtils.isEmpty(activitOrderId)) {
            throw new SystemException("请提交正确的活动订单");
        }
        request.getSession().setAttribute("activityOrderId", activitOrderId);
        String sHtmlText = this.activityOrderService.executePay(activitOrderId);
        ModelAndView mv = new ModelAndView("/admin/activityOrder/confirmPay");
        mv.addObject("sHtmlText", sHtmlText);
        return mv;
    }

    /**
     * 支付成功确认
     *
     * @param request
     * @param response
     * @param activitOrderId
     * @throws IOException
     */
    @RequestMapping(value = "admin/api/activityOrder/{activitOrderId}/payOk")
    public void payOk(HttpServletRequest request, HttpServletResponse response, @PathVariable("activitOrderId") String activitOrderId) throws IOException {
        if (StringUtils.isEmpty(activitOrderId)) {
            throw new SystemException("请提交正确的活动订单");
        }
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activityOrderId", activitOrderId);
        List<Object> objects = this.activityOrderService.getProperty("payState", null, queryParams);
        if (objects.size() <= 0) {
            response.getWriter().print("系统出错，请与管理员联系！");
            return;
        }
        boolean payState = (boolean) objects.get(0);
        if (payState) {
            request.getSession().removeAttribute("activityOrderId");
            response.getWriter().print("恭喜你！,支付成功！,请关闭此提示并及时到“活动订单”菜单中进行核实");
        } else {
            response.getWriter().print("提示！支付中出现问题！可能是你未走完支付流程，" +
                    "或者支付宝支付服务器问题，如果支付宝提示支付成功，请及时联系我们进行核实！如果是未走完支付流程，请关闭此提示，在次进行提交完成支付！");
        }
    }

    /**
     * 支付失败确认
     *
     * @param request
     * @param response
     * @param activitOrderId
     * @throws IOException
     */
    @RequestMapping(value = "admin/api/activityOrder/{activitOrderId}/payErr")
    public void payErr(HttpServletRequest request, HttpServletResponse response, @PathVariable("activitOrderId") String activitOrderId) throws IOException {
        if (StringUtils.isEmpty(activitOrderId)) {
            throw new SystemException("请提交正确的活动订单");
        }
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activityOrderId", activitOrderId);
        List<Object> objects = this.activityOrderService.getProperty("payState", null, queryParams);
        if (objects.size() <= 0) {
            response.getWriter().print("系统出错，请与管理员联系！");
            return;
        }
        boolean payState = (boolean) objects.get(0);
        if (payState) {
            request.getSession().removeAttribute("activityOrderId");
            response.getWriter().print("你好，系统查询出，你此次订单已经支付成功，并未出现支付问题！,请关闭此提示并及时到“活动订单”菜单中进行核实");
        } else {
            response.getWriter().print("提示！支付中出现问题！可能是你未走完支付流程，" +
                    "或者支付宝支付服务器问题，如果支付宝提示支付成功，请及时联系我们进行核实！如果是未走完支付流程，请关闭此提示，在次进行提交完成支付！");
        }
    }

}
