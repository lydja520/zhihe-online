package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.ICouponItemService;
import cn.com.zhihetech.online.service.ICouponService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
@Controller
public class CouponController extends SupportController {
    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "couponService")
    private ICouponService couponService;
    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "couponItemService")
    private ICouponItemService couponItemService;

    @RequestMapping(value = "admin/activity/{activityId}/coupon")
    public ModelAndView indexPage(HttpServletRequest request, @PathVariable String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            throw new SystemException("查询参数不符合");
        }
        ModelAndView mv = new ModelAndView("admin/coupon");
        Merchant merchant = adminService.getMerchant(getCurrentAdmin(request));
        Activity activity = this.activityService.getById(activityId);
        mv.addObject("merchant", merchant);
        mv.addObject("activity", activity);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activity/{activityId}/coupon/list")
    public PageData<Coupon> getActivityCoupon(HttpServletRequest request, @PathVariable String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            throw new SystemException("查询参数不符合");
        }
        String merchantId = this.getCurrentAdmin(request).getMerchant().getMerchantId();
        IQueryParams queryParams = createQueryParams(request).andEqual("activity.activitId", activityId).andEqual("merchant.merchantId", merchantId).andEqual("deleted", false);
        PageData<Coupon> coupons = this.couponService.getPageData(createPager(request), queryParams);
        return coupons;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/coupon/add")
    public ResponseMessage addCoupon(Coupon coupon, String beginValidity, String validity) {
        Date beginDate = new Date(), endDate;
        if (StringUtils.isEmpty(validity)) {
            throw new SystemException("有效结束时间不能为空！");
        }
        if (!StringUtils.isEmpty(beginValidity)) {
            beginDate = DateUtils.String2DateTime(beginValidity);
        }
        endDate = DateUtils.String2DateTime(validity);
        this.couponService.addCoupon(coupon, beginDate, endDate);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/coupon/editInfo")
    public ResponseMessage editCouponInfo(Coupon coupon, String beginValidity, String validity) {
        Date beginDate = new Date(), endDate;
        if (StringUtils.isEmpty(validity)) {
            throw new SystemException("有效结束时间不能为空！");
        }
        if (!StringUtils.isEmpty(beginValidity)) {
            beginDate = DateUtils.String2DateTime(beginValidity);
        }
        endDate = DateUtils.String2DateTime(validity);
        this.couponService.updateBaseInfo(coupon, beginDate, endDate);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/coupon/delete")
    public ResponseMessage deleteCoupon(Coupon coupon) {
        this.couponService.delete(coupon);
        return executeResult();
    }

    @RequestMapping(value = "admin/merchant/allCoupon")
    public String allCouponIndexPage() {
        return "admin/coupon/couponUse";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/coupon/use")
    public ResponseMessage executeSearchCoupon(HttpServletRequest request, String couponCode, String userPhone) {
        Admin admin = this.getCurrentAdmin(request);
        CouponItem couponItem = this.couponItemService.executeUseCoupon(admin, couponCode, userPhone);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "使用成功!", couponItem);
    }

}
