package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.util.common.MD5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2015/12/9.
 */
@Controller
public class AdminPasswordController extends SupportController {

    @Resource(name = "adminService")
    private IAdminService adminService;

    @RequestMapping(value = "admin/changePwd")
    public String pageIndex() {
        return "admin/changePassword";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/admin/changePwd")
    public ResponseMessage changePassword(String oldPwd, String newPwd, HttpServletRequest request) {
        Admin admin = getCurrentAdmin(request);
        this.adminService.changePassword(admin.getAdminCode(), oldPwd,newPwd);
        return executeResult();
    }
}
