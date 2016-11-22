package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.ActivityInvitation;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IActivityInvitationService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2016/3/21.
 */
@Controller
public class ActivityInvitationController extends SupportController {

    @Resource(name = "activityInvitationService")
    private IActivityInvitationService activityInvitationService;

    @RequestMapping(value = "admin/invitation/{activitId}/baseInfo")
    public ModelAndView pageActivityBaseInfo(@PathVariable("activitId") String activitId) {
        ModelAndView modelAndView = new ModelAndView("admin/invitationBaseInfo");
        ActivityInvitation invitation = this.activityInvitationService.getById(activitId);
        modelAndView.addObject("invitation", invitation);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activityInvitation/add", method = RequestMethod.POST)
    public ResponseMessage addInvitation(ActivityInvitation invitation) {
        this.activityInvitationService.add(invitation);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchant/invitations", method = RequestMethod.GET)
    public PageData<ActivityInvitation> getInvitationPageData(HttpServletRequest request) {
        Pager pager = createPager(request);
        return this.activityInvitationService.getInvitationsPageDataByAdminId(pager, getCurrentAdminId(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activityInvitation/agree", method = RequestMethod.POST)
    public ResponseMessage agressInvitation(String invitationId) {
        this.activityInvitationService.updateAgreeInvitation(invitationId);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activityInvitation/refuse", method = RequestMethod.POST)
    public ResponseMessage refuseInvitation(String invitationId) {
        this.activityInvitationService.updateRefuseInvitation(invitationId);
        return executeResult();
    }
}
