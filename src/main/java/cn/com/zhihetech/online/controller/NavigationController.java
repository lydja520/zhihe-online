package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Navigation;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.INavigationService;
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
 * Created by YangDaiChun on 2015/12/21.
 */
@Controller
public class NavigationController extends SupportController {

    @Resource(name = "navigationService")
    private INavigationService navigationService;

    @RequestMapping(value = "admin/navigation")
    public String indexPage() {
        return "admin/navigation";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/navigation/list")
    public PageData<Navigation> getPageData(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        if (!queryParams.sortContainsKey("order")) {
            queryParams.sort("order", Order.ASC);
        }
        return this.navigationService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/navigation/add", method = RequestMethod.POST)
    public ResponseMessage addNavigation(Navigation navigation) {
        this.navigationService.add(navigation);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/navigation/edit", method = RequestMethod.POST)
    public ResponseMessage updateNavigation(Navigation navigation) {
        this.navigationService.update(navigation);
        return executeResult();
    }
}
