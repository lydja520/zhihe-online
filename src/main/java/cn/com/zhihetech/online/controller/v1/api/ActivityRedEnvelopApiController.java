package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IRedEnvelopItemService;
import cn.com.zhihetech.online.service.IRedEnvelopService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/3/2.
 */
@Controller
public class ActivityRedEnvelopApiController extends ApiController {

    @Resource(name = "redEnvelopService")
    private IRedEnvelopService redEnvelopService;
    @Resource(name = "redEnvelopItemService")
    private IRedEnvelopItemService redEnvelopItemService;


    /**
     * <h3>根据商家Id和活动Id获取活动红包列表</h3>
     * url: api/redEnvelop/list  <br>
     * <p>
     * <p>参数</p>
     * merchantId : 商家Id <br>
     * activityId : 活动Id  <br>
     *
     * @param merchantId
     * @param activityId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "redEnvelop/list")
    public PageData<RedEnvelop> getRedEnvelopPageDataById(String merchantId, String activityId, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("merchant.merchantId", merchantId).andEqual("activity.activitId", activityId).andEqual("sended", false).sort("createDate", Order.DESC);
        return this.redEnvelopService.getPageData(this.createPager(request), queryParams);
    }

    /**
     * <h3>修改红包状态为已发出</h3>
     * url : api/redEnvelop/{id}/sended  <br>
     * {id} : 红包ID
     *
     * @param envelopId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "redEnvelop/{id}/sended")
    public ResponseMessage updateSendStateToSended(@PathVariable(value = "id") String envelopId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("envelopId", envelopId);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("sended", true);
        this.redEnvelopService.executeUpdate(paramAndValue, queryParams);
        return executeResult();
    }

    /**
     * <h3>用户抢红包</h3>
     * url : api/redEnvelopItem/get  <br>
     * <p>
     * <p>参数</p>
     * envelopId：红包Id  <br>
     * userId : 用户Id  <br>
     *
     * @param envelopId
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "redEnvelopItem/get")
    public ResponseMessage grabRedEnvelop(String envelopId, String userId) {
        ResponseMessage responseMessage = null;
        responseMessage = this.redEnvelopService.updateGrabRedEnvelop(envelopId, userId);
        if (responseMessage != null) {
            return responseMessage;
        }
        return executeResult();
    }

    /**
     * <h3>根据红包Id获取红包</h3>
     * url : api/redEnvelopItem/{id}/details  <br>
     * {id} : 红包Id  <br>
     *
     * @param envelopItemId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "redEnvelopItem/{id}/details")
    public ResponseMessage RedEnvelopItemDetails(@PathVariable(value = "id") String envelopItemId) {
        if (StringUtils.isEmpty(envelopItemId)) {
            throw new SystemException("查询参数无效");
        }
        RedEnvelopItem redEnvelopItem = this.redEnvelopItemService.getById(envelopItemId);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取成功", redEnvelopItem);
    }

    /**
     * <h3>根据用户ID获取用户抢到的活动红包</h3>
     * url ： api/user/{userId}/redEnvelopItem/list  <br>
     * {userId} : 用户Id <br>
     *
     * @param userId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/redEnvelopItem/list")
    public PageData<RedEnvelopItem> getRedEnvelopsByUserId(@PathVariable(value = "userId") String userId, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("user.userId", userId)
                .sort("extractState", Order.ASC)
                .sort("receivedDate", Order.DESC);
        return this.redEnvelopItemService.getPageData(this.createPager(request), queryParams);
    }


}
