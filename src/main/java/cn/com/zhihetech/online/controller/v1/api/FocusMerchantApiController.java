package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.FocusMerchant;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IFocusMerchantService;
import cn.com.zhihetech.online.service.IMerchantService;
import cn.com.zhihetech.online.service.IUserService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
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
import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/1/1.
 */
@Controller
public class FocusMerchantApiController extends ApiController {

    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    @Resource(name = "userService")
    private IUserService userService;

    @Resource(name = "focusMerchantService")
    private IFocusMerchantService focusMerchantService;

    /**
     * 添加一个商家关注
     * <p>
     * URL: api/focusMerchant/add
     * <p>
     * 参数
     *
     * @param merchantId 商家id
     * @param userId     用户id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "focusMerchant/add", method = RequestMethod.POST)
    public ResponseMessage saveFousMerchant(String merchantId, String userId) {
        Merchant merchant = this.merchantService.getById(merchantId);
        User user = this.userService.getById(userId);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchant", merchant).andEqual("user", user);
        List<FocusMerchant> focusMerchants = this.focusMerchantService.getAllByParams(null, queryParams);
        if (focusMerchants.size() > 0) {
            return executeResult(ResponseStatusCode.SYSTEM_ERROR_CODE, "你已经关注过该商家!");
        }
        FocusMerchant focusMerchant = new FocusMerchant();
        focusMerchant.setFocusMerchantId(null);
        focusMerchant.setMerchant(merchant);
        focusMerchant.setUser(user);
        focusMerchant.setFocusDate(new Date());
        this.focusMerchantService.add(focusMerchant);
        return executeResult();
    }


    /**
     * <h3>根据用户ID获取用户所关注的商家</h3>
     * url: api/user/{userId}/focusMerchants <br>
     * {userId}:用户Id
     *
     * @param userId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/focusMerchants")
    public PageData<Merchant> getFocusMerchantsByUserId(@PathVariable(value = "userId") String userId, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("user.userId", userId)
                .andEqual("merchant.permit", true)
                .sort("focusDate", Order.DESC);
        return this.focusMerchantService.getMerchantPageData(this.createPager(request), queryParams);
    }

    /**
     * 检查是否关注过了该商家
     * <p>
     * URL：api/checkFocus
     * <p>
     * 参数
     *
     * @param merchantId 商家ID
     * @param userId     用户ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkFocus")
    public ResponseMessage checkFocusMerchant(String merchantId, String userId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchant.merchantId", merchantId).andEqual("user.userId", userId);
        List<FocusMerchant> focusMerchants = this.focusMerchantService.getAllByParams(null, queryParams);
        if (focusMerchants.size() > 0) {
            return executeResult("你已经关注过该商家!");
        }
        return executeResult(ResponseStatusCode.SYSTEM_ERROR_CODE, "你还未关注过该商家");
    }

    /**
     * <h3>取消商家收藏</h3>
     * url : api/cancelFocusMerchant  <br>
     * <p>
     * <p>
     * <h4>参数</h4>
     * userId: 用户Id   <br>
     * merchantId:  商家Id  <br>
     *
     * @param userId
     * @param merchantId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cancelFocusMerchant", method = RequestMethod.POST)
    public ResponseMessage cancelFocusMerchant(String userId, String merchantId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("user.userId", userId).andEqual("merchant.merchantId", merchantId);
        List<FocusMerchant> focusMerchants = this.focusMerchantService.getAllByParams(null, queryParams);
        if (focusMerchants.size() == 0) {
            return executeResult(ResponseStatusCode.HAVE_NO_FOCUS, "关注已经取消，无需重复操作");
        }
        this.focusMerchantService.delete(focusMerchants.get(0));
        return executeResult();
    }

}
