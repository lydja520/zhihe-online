package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.UserWithdraw;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IMessagerService;
import cn.com.zhihetech.online.service.IUserService;
import cn.com.zhihetech.online.service.IUserWithdrawServie;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YangDaiChun on 2016/3/12.
 */
@Controller
public class UserWithdrawApiController extends ApiController {

    @Resource(name = "userWithdrawService")
    private IUserWithdrawServie userWithdrawServie;
    @Resource(name = "userService")
    private IUserService userService;
    @Resource(name = "messagerService")
    private IMessagerService messagerService;

    /**
     * <h3>用户申请钱包提现操作</h3>
     * url: api/userWithdraw/apply <br>
     * <p>
     * <p>参数</p>
     * userId : 用户Id  <br>
     * money : 需要提现的金额  <br>
     * securityCode: 短信验证码  <br>
     * aliCode : 支付宝账号  <br>
     *
     * @param userId
     * @param money
     * @param securityCode
     * @param aliCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "userWithdraw/apply", method = RequestMethod.POST)
    public ResponseMessage applyWithdraw(String userId, float money, String securityCode, String aliCode) {
        float surplusMoney = this.userWithdrawServie.addApplyWithdraw(userId, money, securityCode, aliCode);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "申请提交成功", surplusMoney);
    }

    /**
     * <h3>根据用户Id获取用户的提现记录</h3>
     * url : api/user/{userId}/withdrawRecord  <br>
     * {userId} : 用户ID  <br>
     *
     * @param userId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/withdrawRecord")
    public PageData<UserWithdraw> getUserWithdrawRecord(@PathVariable(value = "userId") String userId, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("user.userId", userId).sort("applyDate", Order.DESC);
        return this.userWithdrawServie.getPageData(this.createPager(request), queryParams);
    }
}
