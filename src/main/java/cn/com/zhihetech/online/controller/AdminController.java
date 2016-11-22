package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.Role;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.IRoleService;
import cn.com.zhihetech.util.common.MD5Utils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/18.
 */
@Controller
public class AdminController extends SupportController {
    @Resource(name = "adminService")
    private IAdminService adminService;

    @Resource(name = "roleService")
    private IRoleService roleService;

    @RequestMapping(value = "admin/admin")
    public ModelAndView indexPage() {
        ModelAndView modelAndView = new ModelAndView("admin/admin");
        List<Role> roles = this.roleService.getAllByParams(null, null);
        modelAndView.addObject("roleList", roles);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/admin/list")
    public PageData<Admin> getAdminPageData(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request).andIsNull("merchant");
        return this.adminService.getPageData(createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/admin/edit", method = RequestMethod.POST)
    public ResponseMessage editAdmin(Admin admin, String[] roleIds) {
        if (roleIds != null) {
            for (String id : roleIds) {
                if (StringUtils.isEmpty(id)) {
                    continue;
                }
                Role role = new Role();
                role.setRoleId(id);
                admin.getRoles().add(role);
            }
        }
        this.adminService.update(admin);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/admin/add", method = RequestMethod.POST)
    public ResponseMessage addAdmin(Admin admin, String[] roleIds) {
        if (roleIds != null) {
            for (String id : roleIds) {
                if (StringUtils.isEmpty(id)) {
                    continue;
                }
                Role role = new Role();
                role.setRoleId(id);
                admin.getRoles().add(role);
            }
        }
        if (StringUtils.isEmpty(admin.getAdminPwd())) {
            admin.setAdminPwd(MD5Utils.getMD5Msg(Constant.DEFAULT_PASSWORD));
        } else {
            admin.setAdminPwd(MD5Utils.getMD5Msg(admin.getAdminPwd()));
        }
        this.adminService.addOfficialAdmin(admin);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/admin/disableAdmin", method = RequestMethod.POST)
    public ResponseMessage disableAdmin(Admin admin) {
        this.adminService.disableAdmin(admin);
        return this.executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/admin/resetPwd", method = RequestMethod.POST)
    public ResponseMessage resetPwd(Admin admin) {
        admin.setAdminPwd(MD5Utils.getMD5Msg(Constant.DEFAULT_PASSWORD));
        this.adminService.resetPwd(admin);
        return this.executeResult();
    }

}
