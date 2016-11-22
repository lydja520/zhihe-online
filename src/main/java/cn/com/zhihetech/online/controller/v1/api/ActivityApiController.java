package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IActivityFansService;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.service.IMerchantAllianceService;
import cn.com.zhihetech.util.hibernate.*;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
@Controller
public class ActivityApiController extends ApiController {
    @Resource(name = "activityService")
    private IActivityService activityService;

    @Resource(name = "merchantAllianceService")
    private IMerchantAllianceService merchantAllianceService;

    @Resource(name = "activityFansService")
    private IActivityFansService activityFansService;

    /**
     * <h3>获取所有活动</h3>
     * <p>
     * url : api/activity/list  <br>
     * <p>
     * <h4>参数 </h4>
     * searchName:  搜索字段名
     * searchValue:   搜索的字段值
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "activity/list")
    public PageData<Activity> getActiviting(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        SubQueryParams subQuery = new SubQueryParams();
        subQuery.andEqual("currentState", Constant.ACTIVITY_STATE_STARTED).orEqual("currentState", Constant.ACTIVITY_STATE_EXAMINED_OK);
        queryParams.andSubParams(subQuery).sort("currentState", Order.DESC).sort("beginDate",Order.DESC);
        PageData<Activity> data = this.activityService.getPageData(createPager(request), queryParams);
        for (Activity activity : data.getRows()) {
            String merchantId = activity.getActivityPromoter().getMerchantId();
            Merchant merchant = new Merchant();
            merchant.setMerchantId(merchantId);
            activity.setActivityPromoter(merchant);
        }
        return data;
    }

    /**
     * 根据Id获取对应商家的活动（已审核和正在开始的活动）
     * <p>
     * URL: api/merchant/{merchantId}/activities  <br>
     *
     * @param merchantId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "merchant/{merchantId}/activities")
    public PageData<Activity> getValidActivitiesByMerchantId(@PathVariable(value = "merchantId") String merchantId, HttpServletRequest request) {
        List<Object> states = new ArrayList<>();
        states.add(Constant.ACTIVITY_STATE_STARTED);
        states.add(Constant.ACTIVITY_STATE_EXAMINED_OK);
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andIn("currentState", states)
                .sort("currentState", Order.DESC);
        return this.merchantAllianceService.getValidActivitiesByMerchantId(this.createPager(request), queryParams, merchantId);
    }

    /**
     * 根据Id获取对应商家的正在进行的活动 <br>
     * <p>
     * URL: api/merchant/{merchantId}/startedActivity/list <br>
     *
     * @param merchantId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "merchant/{merchantId}/startedActivity/list")
    public PageData<Activity> getStartActivitiesByMerchantId(@PathVariable(value = "merchantId") String merchantId, HttpServletRequest request) {
        return this.merchantAllianceService.getMerchantStartedActivities(createPager(request), merchantId);
    }

    /**
     * <h3>根据活动Id获取活动基本信息</h3>
     * url: api/activity/{id}
     *
     * @param activityId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "activity/{id}")
    public ResponseMessage getActivitysById(@PathVariable(value = "id") String activityId) {
        Activity activity = this.activityService.getById(activityId);
        if (activity != null) {
            return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取成功", activity);
        }
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "不存在该活动！", null);
    }

    /**
     * <h3>根据活动Id获取商家联盟</h3>
     * url : api/activity/{activityId}/merchantAlliances
     *
     * @param activityId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "activity/{activityId}/merchantAlliances")
    public List<MerchantAlliance> getMerchantAlliancesByActivityId(@PathVariable(value = "activityId") String activityId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activity.activitId", activityId);
        return this.merchantAllianceService.getAllByParams(null, queryParams);
    }

    /**
     * 根据商品的类别获取对应类型的活动  <br>
     * URL: api/goodsAttributeSet/{goodsAttSetId}/activities
     * <p>
     * {goodsAttSetId}:商品分类Id
     *
     * @param goodsAttSetId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodsAttributeSet/{goodsAttSetId}/activities")
    public PageData<Activity> getActivitiesByGoodeAttSet(@PathVariable(value = "goodsAttSetId") String goodsAttSetId, HttpServletRequest request) {
        return this.activityService.getActivitiesByGoodsAttSetId(goodsAttSetId, createPager(request));
    }

    /**
     * <h3>添加单个用户到活动中</h3>
     * url：api/activityFans/add <br>
     * <p>
     * <p>参数</p>
     * activityId：活动Id<br>
     * userId : 用户Id <br>
     * <p>
     * <p>返回状态码</p>
     * <p>
     * public final static int FORBID_ADD = 810;当前活动状态不支持加入操作<br>
     * public final static int CHATROOM_ADD_SINGLE_USER_FAILURE = 815;添加活动失败，请稍后再试<br>
     * public final static int USER_ALREADY_IN_ACTIVITY_FANS = 820;用户已经加入到该活动中，无需再次加入<br>
     *
     * @param activityId
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "activityFans/add")
    public ResponseMessage addSingleUserToActivityChatRoom(String activityId, String userId) {
        ResponseMessage responseMessage = this.activityService.addSingleUserToActivityChatRoom(activityId, userId);
        if (responseMessage != null) {
            return responseMessage;
        }
        return executeResult();
    }

    /**
     * 得到聊天室的详细信息
     *
     * @param activityId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "chatRoom/{activityId}/info")
    public ResponseMessage getChatRoomDetail(@PathVariable(value = "activityId") String activityId) {
        return this.activityService.getChatRoomInfo(activityId);
    }

    /**
     * <h3>根据活动Id获取参与当前活动的用户</h3>
     * url : api/activity/{activityId}/activityFans <br>
     * {activityId}: 活动Id <br>
     *
     * @param activityId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "activity/{activityId}/activityFans")
    public PageData<Object> getActivityFansByActivityId(@PathVariable(value = "activityId") String activityId, HttpServletRequest request) {
        Activity activity = new Activity();
        activity.setActivitId(activityId);
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("activity", activity);
        return this.activityFansService.getPageDataProperty("fans", this.createPager(request), queryParams);
    }
}
