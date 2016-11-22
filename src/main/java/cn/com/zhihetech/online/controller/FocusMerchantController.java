package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.FocusMerchant;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.service.IFocusMerchantService;

import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/12/18.
 */
@Controller
public class FocusMerchantController extends SupportController {
    @Resource(name = "focusMerchantService")
    private IFocusMerchantService iFocusMerchantService;

    @RequestMapping(value = "admin/focusMerchant")
    public String getPage() {
        return "admin/focusMerchant";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/focusMerchant/list")
    public PageData<FocusMerchant> getPageData(HttpServletRequest request) {
        Admin currentAdmin = (Admin) request.getSession().getAttribute(CURRENT_ADMIN);
        GeneralQueryParams generalQueryParams = new GeneralQueryParams();
        generalQueryParams.andEqual("merchant.merchantId", currentAdmin.getMerchant().getMerchantId());
        return this.iFocusMerchantService.getPageData(createPager(request), createQueryParams(request, generalQueryParams));
    }

    @RequestMapping(value = "admin/focusMerchant/Info/{id}")
    public ModelAndView getUserInfo(@PathVariable(value = "id") String id, HttpServletRequest request) {
        IQueryParams iQueryParams = new GeneralQueryParams();
        iQueryParams.andEqual("merchant.merchantId", id);
        List<FocusMerchant> focusMerchants = this.iFocusMerchantService.getAllByParams(createPager(request), iQueryParams);
        List<User> users = new ArrayList<User>();
        for (FocusMerchant focusMerchant : focusMerchants) {
            users.add(focusMerchant.getUser());
        }
        ModelAndView modelAndView = new ModelAndView("admin/focusMerchantInfo");
        modelAndView.addObject("user", users);
        return modelAndView;
    }
}
