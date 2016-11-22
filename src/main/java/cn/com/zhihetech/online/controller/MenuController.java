package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Menu;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IMenuService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
@Controller
public class MenuController extends SupportController {

    @Resource(name = "menuService")
    private IMenuService menuService;

    @RequestMapping(value = "admin/menu")
    public String menuPage() {
        return "admin/menu";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/menu/list")
    public PageData<Menu> getMenuPageData(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request);
        if(!queryParams.sortContainsKey("menuOrder")){
            queryParams.sort("menuOrder", Order.ASC);
        }
        return this.menuService.getPageData(createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/menu/add", method = RequestMethod.POST)
    public ResponseMessage addMenu(Menu menu) {
        if (menu.getParentMenu() != null && StringUtils.isEmpty(menu.getParentMenu().getMenuId())) {
            menu.setParentMenu(null);
            menu.setIsRoot(true);
        }
        if(StringUtils.isEmpty(menu.getMenuId())){
            menu.setMenuId(null);
        }
        this.menuService.add(menu);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/menu/edit", method = RequestMethod.POST)
    public ResponseMessage editMenu(Menu menu) {
        if (menu.getParentMenu() != null && StringUtils.isEmpty(menu.getParentMenu().getMenuId())) {
            menu.setParentMenu(null);
            menu.setIsRoot(true);
        }
        this.menuService.update(menu);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/menu/rootList")
    public List<Menu> getRootMenus() {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("isRoot", true).andEqual("permit", true).andIsNull("parentMenu").sort("menuOrder",Order.ASC);
        return this.menuService.getAllByParams(null, queryParams);
    }

    @RequestMapping(value = "admin/api/menu/img")
    public String selectMenuImg(){
        return "admin/selectMenuImg";
    }
}
