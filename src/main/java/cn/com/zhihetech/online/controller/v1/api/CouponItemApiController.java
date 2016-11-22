package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.CouponItem;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.ICouponItemService;
import cn.com.zhihetech.online.service.ICouponService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by YangDaiChun on 2016/3/16.
 */
@Controller
public class CouponItemApiController extends ApiController {

    @Resource(name = "couponItemService")
    private ICouponItemService couponItemService;
    @Resource(name = "couponService")
    private ICouponService couponService;

    /**
     * <h3>根据id获取优惠券详细信息</h3>
     * url ： api/couponItem/{couponItemId}/details  <br>
     *
     * @param couponItemId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "couponItem/{couponItemId}/details")
    public ResponseMessage getCouponItemById(@PathVariable(value = "couponItemId") String couponItemId) {
        CouponItem couponItem = this.couponItemService.getById(couponItemId);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取成功", couponItem);
    }

    /**
     * <h3>根据用户Id获取优惠券列表</h3>
     * url: api/user/{userId}/couponItem/list  <br>
     *
     * @param userId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/couponItem/list")
    public PageData<CouponItem> getCouponPageData(@PathVariable(value = "userId") String userId, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request)
                .andEqual("user.userId", userId)
                .andMoreAndEq("coupon.endValidity", new Date())
                .andEqual("deleted", false)
                .sort("useState", Order.DESC)
                .sort("receivedDate", Order.DESC);
        PageData<CouponItem> couponItemPageData = couponItemService.getPageData(this.createPager(request), queryParams);
        return couponItemPageData;
    }

}
