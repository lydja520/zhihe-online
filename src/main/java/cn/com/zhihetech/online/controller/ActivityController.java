package cn.com.zhihetech.online.controller;


import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by ShenYunjie on 2015/12/9.
 */
@Controller
public class ActivityController extends SupportController {

    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "merchantAllianceService")
    private IMerchantAllianceService merchantAllianceService;
    @Resource(name = "redEnvelopService")
    private IRedEnvelopService redEnvelopService;
    @Resource(name = "couponService")
    private ICouponService couponService;
    @Resource(name = "activityGoodsService")
    private IActivityGoodsService activityGoodsService;
    @Resource(name = "activityFansService")
    private IActivityFansService activityFansService;

    /**
     * 移动端请求活动主页
     *
     * @return
     */
    @RequestMapping(value = "web/activity/{id}")
    public ModelAndView activityInfo(@PathVariable String id) {
        ModelAndView mv = new ModelAndView("mobile/activityinfo");
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activity.activitId", id);
        List<MerchantAlliance> merchantAlliances = this.merchantAllianceService.getAllByParams(null, queryParams);
        IQueryParams queryParams1 = new GeneralQueryParams();
        queryParams1.andEqual("activity.activitId", id);
        List<ActivityGoods> activityGoodses = this.activityGoodsService.getAllByParams(null, queryParams1);
        for (ActivityGoods activityGoods : activityGoodses) {
            if (activityGoods.getSku().getCoverImg() == null) {
                activityGoods.getSku().setCoverImg(activityGoods.getGoods().getCoverImg());
            }
        }
        queryParams.sort("couponType", Order.ASC);
        List<Coupon> coupons = this.couponService.getAllByParams(null, queryParams);
        mv.addObject("activity", activityService.getById(id));
        mv.addObject("merchantAlliances", merchantAlliances);
        mv.addObject("coupons", coupons);
        mv.addObject("activityGoodses", activityGoodses);
        return mv;
    }

    @RequestMapping(value = "admin/activity")
    public String pageIndex() {
        return "admin/activityList";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activity/list")
    public PageData<Activity> getAllActivityList(HttpServletRequest request) {
        return this.activityService.getPageData(createPager(request), createQueryParams(request));
    }

    @RequestMapping(value = "admin/waitExaminActivity")
    public String waitExaminPage() {
        return "admin/activityWaitExamin";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/waitExaminActivity/list")
    public PageData<Activity> getWaitExaminActivityPageData(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("currentState", Constant.ACTIVITY_STATE_SBUMITED).sort("createDate", Order.DESC);
        return this.activityService.getPageData(this.createPager(request), queryParams);
    }

    @RequestMapping(value = "admin/activity/{id}/info")
    public ModelAndView activityInfo(@PathVariable(value = "id") String id, HttpServletRequest request) {
        Activity activity = this.activityService.getById(id);
        IQueryParams iQueryParams = createQueryParams(request);
        iQueryParams.andEqual("activity.activitId", activity.getActivitId());
        PageData<Merchant> merchants = this.merchantAllianceService.getJoinMerchByActivity(null, iQueryParams);
        Map<String, List<RedEnvelop>> redEnvelops = this.redEnvelopService.getAbleRedEnvelopByMerch(createPager(request), merchants.getRows(), activity.getActivitId());
        Map<String, List<Coupon>> coupon = this.couponService.getActivityCoupon(createPager(request), merchants.getRows(), activity.getActivitId());
        Map<String, List<ActivityGoods>> activityGoods = this.activityGoodsService.getActivityGoods(createPager(request), merchants.getRows(), activity.getActivitId());
//        Map<String,List<FocusMerchant>> focusMerchants = this.focusMerchantService.getMerchantFocusUser(createPager(request),merchants.getRows(),activity.getActivitId());
        Map<String, List<ActivityFans>> activityFans = this.activityFansService.getFansUser(createPager(request), merchants.getRows(), activity.getActivitId());

        //TODO:每个商家的红包总数和总额
//        Map total = new HashMap<String,int[]>();
//        Iterator<String> iter = redEnvelops.keySet().iterator();
//        while(iter.hasNext()){
//            float money=0,number=0;
//           StringBuffer keyBuffer = new StringBuffer(iter.next());
//            String key = keyBuffer.toString();
//            List<RedEnvelop> envelops = redEnvelops.get(key);
//            for(RedEnvelop redEnvelop : envelops){
//                money = money + redEnvelop.getTotalMoney();
//                number = number + redEnvelop.getNumbers();
//            }
//            total.put(key,new float[]{money,number});
//        }

        ModelAndView model = new ModelAndView("admin/merchantActivityInfo");
        model.addObject("merchants", merchants);
        model.addObject("redEnvelops", redEnvelops);
        model.addObject("activity", activity);
//        model.addObject("total",total);//红包总额和红包总个数
        model.addObject("coupon", coupon);
        model.addObject("activityGoods", activityGoods);
//        model.addObject("focusMerchants",focusMerchants);//每一个商家的所有关注用户
        model.addObject("activityFans", activityFans);

        return model;
    }

    @ResponseBody
    @RequestMapping(value = "admin/activity/updateActivityExaminState", method = RequestMethod.POST)
    public ResponseMessage examinActivityOk(String activityId, String currentState, String examinMsg) {
        ResponseMessage responseMessage = this.activityService.updateActivityExaminState(activityId, currentState, examinMsg);
        if (responseMessage != null) {
            return responseMessage;
        }
        return executeResult();
    }

    /**
     * 获取轮播图可跳转的活动
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/activitya/banner/able")
    public PageData<Activity> getBannerActivity(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request);
        return this.activityService.getExaminedAndStartedActivities(createPager(request), queryParams);
    }
}
