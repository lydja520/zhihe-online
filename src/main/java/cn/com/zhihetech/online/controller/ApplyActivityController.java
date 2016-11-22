package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.util.PropertiesUtils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.protocol.ExecutionContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by ShenYunjie on 2015/12/7.
 */
@Controller
public class ApplyActivityController extends SupportController {

    @Resource(name = "activityService")
    private IActivityService activityService;

    @RequestMapping(value = "admin/applyActivity")
    public String pageIndex() {
        return "admin/applyActivity";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/applyActivity/list")
    public PageData<Activity> getPageData(HttpServletRequest request) {
        GeneralQueryParams queryParams = createQueryParams(request);
        queryParams.andNotEq("currentState", Constant.ACTIVITY_STATE_FNISHED);
        return this.activityService.getPageDataByAdmin(createPager(request), queryParams, getCurrentAdmin(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activity/apply", method = RequestMethod.POST)
    public ResponseMessage addActivity(Activity activity, HttpServletRequest request, String[] attributs) {
        Set<GoodsAttributeSet> attributeSets = new HashSet<>();
        if (attributs != null && attributs.length > 0) {
            for (String attibutId : attributs) {
                if (!StringUtils.isEmpty(attibutId)) {
                    attributeSets.add(new GoodsAttributeSet(attibutId));
                }
            }
        }
        if (attributeSets.isEmpty()) {
            return executeResult(500, false, "活动对应的商品类别不能为空!");
        }
        activity.setAttributeSets(attributeSets);
        activity.setActivitId(null);    //为添加一个新的活动默认ID为空
        Date startDate = activity.getBeginDate();
        if (startDate == null) {
            return executeResult(500, false, "活动开始时间不能为空！");
        }
        if (startDate.before(new Date())) {
            return executeResult(500, false, "活动开始时间不能小于当前时间！");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int duration = 30;
        try {
            duration = Integer.parseInt(PropertiesUtils.getProperties().getProperty("ACTIVITY_DURATION"));
        } catch (Exception e) {
        }
        calendar.add(Calendar.MINUTE, duration);
        activity.setEndDate(calendar.getTime());
        ImgInfo coverImg = activity.getCoverImg();
        if (coverImg == null || StringUtils.isEmpty(coverImg.getImgInfoId())) {
            activity.setCoverImg(null);
        }
        this.activityService.saveByAdmin(activity, getCurrentAdmin(request));
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activity/editApply", method = RequestMethod.POST)
    public ResponseMessage editApply(Activity activity, String[] attributs) {
        List<String> attributIds = new ArrayList<>();
        if (attributs != null && attributs.length > 0) {
            for (String attibutId : attributs) {
                if (!StringUtils.isEmpty(attibutId)) {
                    attributIds.add(attibutId);
                }
            }
        }
        if (attributIds.isEmpty()) {
            return executeResult(500, false, "活动商品类别能为空！");
        }

        Date startDate = activity.getBeginDate();
        if (startDate == null) {
            return executeResult(500, false, "活动开始时间不能为空！");
        }
        if (startDate.before(new Date())) {
            return executeResult(500, false, "活动开始时间不能小于当前时间！");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int duration = 30;
        try {
            duration = Integer.parseInt(PropertiesUtils.getProperties().getProperty("ACTIVITY_DURATION"));
        } catch (Exception e) {
        }
        calendar.add(Calendar.MINUTE, duration);
        activity.setEndDate(calendar.getTime());
        this.activityService.executeActivity(activity, attributIds);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activity/submitAudit")
    public ResponseMessage submitAudit(String activitId, int currentState) {
        if (StringUtils.isEmpty(activitId)) {
            return executeResult(ResponseStatusCode.SYSTEM_ERROR_CODE, false, "提交参数错误！");
        }
        if (currentState == Constant.ACTIVITY_STATE_UNSBUMIT || currentState == Constant.ACTIVITY_STATE_EXAMINED_NOT) {
            this.activityService.updateSubmitState(activitId);
            return executeResult();
        } else {
            return executeResult(ResponseStatusCode.SYSTEM_ERROR_CODE, false, "已提交过申请，请勿重复提交!");
        }
    }
}
