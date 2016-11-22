package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Menu;
import cn.com.zhihetech.online.bean.Role;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IMenuService;
import cn.com.zhihetech.online.service.IRoleService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/9/6.
 */
@Controller
public class RoleController extends SupportController {

    @Resource(name = "roleService")
    private IRoleService roleService;

    @Resource(name = "menuService")
    private IMenuService menuService;

    @RequestMapping(value = "admin/role")
    public ModelAndView indexPage() {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("permit",true).andNotNull("parentMenu");
        List<Menu> menus = this.menuService.getAllByParams(null, queryParams);
        ModelAndView modelAndView = new ModelAndView("admin/role","menuList",menus);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/role/list")
    public PageData<Role> getRolePageData(HttpServletRequest request) {
        return this.roleService.getPageData(createPager(request), createQueryParams(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/role/roles")
    public List<Role> getRoles() {
        IQueryParams queryParams = new GeneralQueryParams();
        return roleService.getAllByParams(null, queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/role/saveOrUpdate", method = RequestMethod.POST)
    public ResponseMessage addRole(String[] menuIds, Role role) {
        if(StringUtils.isEmpty(role.getRoleId())){
            role.setRoleId(null);
        }
        if (menuIds != null) {
            Set<Menu> menus = new HashSet<>();
            for (String menu : menuIds) {
                Menu m = new Menu();
                m.setMenuId(menu);
                menus.add(m);
            }
            role.setMenus(menus);
        }
        this.roleService.saveOrUpdate(role);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/role/delete")
    public ResponseMessage deleteRole(Role role) {
        this.roleService.delete(role);
        return executeResult();
    }
    @ResponseBody
    @RequestMapping(value = "admin/api/role/modify")
    public ResponseMessage editRole(Role role,String[] menuIds) {
        if (menuIds != null) {
            Set<Menu> menus = new HashSet<>();
            for (String menu : menuIds) {
                Menu m = new Menu();
                m.setMenuId(menu);
                menus.add(m);
            }
            role.setMenus(menus);
        }
        this.roleService.update(role);
        return executeResult();

    }

    @ResponseBody
    @RequestMapping(value = "admin/api/role/menus")
    public List<Menu> getMenuList() {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("permit", true).andNotNull("parentMenu");
        List<Menu> menus = this.menuService.getAllByParams(null, queryParams);
        return menus;
    }
}

