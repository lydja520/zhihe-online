package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.IRedEnvelopService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2015/12/11.
 */
@Controller
public class RedEnvelopController extends SupportController {
    @Resource(name = "redEnvelopService")
    private IRedEnvelopService redEnvelopService;
    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "activityService")
    private IActivityService activityService;

    @RequestMapping("admin/activity/{activitId}/redEnvelop")
    public ModelAndView indexPage(HttpServletRequest request, @PathVariable String activitId) {
        ModelAndView mv = new ModelAndView("admin/merchActivityRedEnvelop");
        Merchant merchant = this.adminService.getMerchant(getCurrentAdmin(request));
        Activity activity = this.activityService.getById(activitId);
        mv.addObject("merchant", merchant);
        mv.addObject("activity", activity);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/redEnvelop/add", method = RequestMethod.POST)
    public ResponseMessage addRedEnvelop(RedEnvelop redEnvelop) {
        this.redEnvelopService.add(redEnvelop);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activity/{activityId}/redEnvelop/list")
    public PageData<RedEnvelop> getActivityRedEnvelopByAdmin(HttpServletRequest request, @PathVariable String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            throw new SystemException("查询参数不足");
        }
        GeneralQueryParams queryParams = (GeneralQueryParams) new GeneralQueryParams()
                .andEqual("activity.activitId", activityId);
        return this.redEnvelopService.getPageDataByAdmin(createPager(request), queryParams, getCurrentAdmin(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/redEnvelop/delete", method = RequestMethod.POST)
    public ResponseMessage deleteRedEnvelop(RedEnvelop redEnvelop) {
        this.redEnvelopService.delete(redEnvelop);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/redEnvelop/updateInfo", method = RequestMethod.POST)
    public ResponseMessage updateBaseInfo(RedEnvelop redEnvelop) {
        this.redEnvelopService.updateBaseInfo(redEnvelop.getEnvelopId(), redEnvelop.getTotalMoney(), redEnvelop.getNumbers(), redEnvelop.getEnvelopMsg());
        return executeResult();
    }


}
