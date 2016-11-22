package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.dao.IActivityDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityOrderDetailService;
import cn.com.zhihetech.online.service.IActivityOrderService;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.service.IMerchantAllianceService;
import cn.com.zhihetech.online.vo.ActivityOrderAndActivityOrderDeatils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/12/10.
 */
@Controller
public class MerchantAllianceController extends SupportController {

    @Resource(name = "merchantAllianceService")
    private IMerchantAllianceService merchantAllianceService;
    @Resource(name = "activityOrderService")
    private IActivityOrderService activityOrderService;
    @Resource(name = "acitivityOrderDetailService")
    private IActivityOrderDetailService activityOrderDetailService;
    @Resource(name = "activityService")
    private IActivityService activityService;

    @RequestMapping("admin/activity/{activId}/merchAlliance")
    public ModelAndView pageIndex(@PathVariable String activId) {
        ModelAndView mv = new ModelAndView("admin/merchantAlliance");
        Map queryParam = new HashMap<>();
        queryParam.put("activitId", activId);
        mv.addObject("currentActivity", queryParam);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activity/merchAlliance/list")
    public PageData<MerchantAlliance> getMerchAllianceByActivity(HttpServletRequest request, String activitId) {
        return this.merchantAllianceService.getMerchAllianceByActivity(createPager(request), createQueryParams(request), activitId);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchAlliance/ableList")
    public PageData<Merchant> getAbleMerchByActivity(HttpServletRequest request, String activitId) {
        return this.merchantAllianceService.getAbleMerchByActivity(createPager(request), createQueryParams(request), activitId);
    }

    /**
     * 添加商家到活动中
     *
     * @param merchantAlliance
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchAlliance/add")
    public ResponseMessage addMerchAlliance(MerchantAlliance merchantAlliance) {
        merchantAlliance.setAllianceState(MerchantAlliance.ALLIANCE_UNEXECUTED_STATE);
        merchantAlliance.setCreateDate(new Date());
        merchantAlliance.setActivityBudget(0f);
        this.merchantAllianceService.add(merchantAlliance);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchAlliance/delete")
    public ResponseMessage deleteMerchAlliance(MerchantAlliance merchantAlliance) {
        this.merchantAllianceService.delete(merchantAlliance);
        return executeResult();
    }

}
