package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.ICouponService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by YangDaiChun on 2016/3/16.
 */
@Controller
public class CouponApiController extends ApiController {

    @Resource(name = "couponService")
    private ICouponService couponService;


    /**
     * <h3>用户抢优惠券</h3>
     * url : api/coupon/grab  <br>
     * <p>
     * <p>参数：</p>
     * couponId：优惠券id  <br>
     * userId：用户id <br>
     *
     * @param couponId
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "coupon/grab", method = RequestMethod.POST)
    public ResponseMessage grabCoupon(String couponId, String userId) {
        ResponseMessage responseMessage = this.couponService.updateCouponItem(couponId, userId);
        if (responseMessage != null) {
            return responseMessage;
        }
        return executeResult();
    }
}
