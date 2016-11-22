package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.IMerchantAllianceService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2015/12/11.
 */
@Controller
public class MerchActivityController extends SupportController {
    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "merchantAllianceService")
    private IMerchantAllianceService merchantAllianceService;
    @Resource(name = "adminService")
    private IAdminService adminService;

    @RequestMapping(value = "admin/merchActivity")
    public ModelAndView indexPage(HttpServletRequest request) {
        Admin admin = getCurrentAdmin(request);
        ModelAndView mv = new ModelAndView("admin/merchantActivity");
        Merchant merchant = this.adminService.getMerchant(admin);
        mv.addObject("merchant", merchant);
        return mv;
    }

    /**
     * 获取商家参与的所有活动（已审核，已开始和已结束的所有活动）
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/api/merchActivity/list")
    public PageData<Activity> getMerchActivities(HttpServletRequest request) {
        GeneralQueryParams queryParams = createQueryParams(request);
        //queryParams.andNotEq("currentState", Constant.ACTIVITY_STATE_FNISHED);
        if (!queryParams.queryContainsKey("beginDate")) {
            queryParams.sort("beginDate", Order.DESC);
        }
        return this.merchantAllianceService.getActivityListByAdmin(createPager(request), queryParams, getCurrentAdmin(request));
    }
}
