package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.util.common.MD5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2015/11/16.
 */
@Controller
public class AdminLoginController extends SupportController {

    @Resource(name = "adminService")
    private IAdminService adminService;

    @RequestMapping(value = "admin/login", method = RequestMethod.GET)
    public String loginPage(HttpServletRequest request) {
        if (getCurrentAdmin(request) != null) {
            return "redirect:/admin/index";
        }
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "admin/login", method = RequestMethod.POST)
    public ResponseMessage adminLogin(Admin admin, HttpServletRequest request) {
        Admin loginAdmin = this.adminService.executeLogin(admin.getAdminCode(), MD5Utils.getMD5Msg(admin.getAdminPwd()));
        if (loginAdmin == null) {
            return executeResult(500, "用户名或密码错误，请重试");
        }
        setCurrentAdmin(request, loginAdmin);
        String merchantId = this.adminService.getMerchantId(loginAdmin);
        setCurrentMerchatId(request, merchantId);
        return executeResult("登录成功");
    }

    @RequestMapping(value = "admin/logout", method = RequestMethod.GET)
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

}
