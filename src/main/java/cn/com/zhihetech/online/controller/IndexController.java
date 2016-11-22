package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Menu;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.IShopShowService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
@Controller
public class IndexController extends SupportController {

    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "shopShowService")
    private IShopShowService shopShowService;

    @RequestMapping("admin/index")
    public ModelAndView indexPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("admin/index");
        if (getCurrentAdmin(request) == null) {
            modelAndView.setViewName("redirect:/admin/login");
            return modelAndView;
        }
        List<Menu> menus = this.adminService.getMenusByAndmin(getCurrentAdmin(request));
        modelAndView.addObject("menus", menus);
        modelAndView.addObject("admin", getCurrentAdmin(request));
        return modelAndView;
    }

    @RequestMapping("admin/notify")
    public ModelAndView notifyPage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("admin/notify");
        String merchantId = this.getCurrentMerchatId(request);
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("merchant.merchantId", merchantId);
        long totalShopShow = this.shopShowService.getRecordTotal(queryParams);
        mv.addObject("totalShopShow", totalShopShow);
        return mv;
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcomePage(HttpServletRequest request) {
        return "admin/welcome";
    }

    @RequestMapping("/jeasyui")
    public String jeasyuiPage() {
        return "merchant";
    }
}
