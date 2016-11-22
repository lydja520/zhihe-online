package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.Navigation;
import cn.com.zhihetech.online.controller.SupportController;
import cn.com.zhihetech.online.service.INavigationService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
@Controller
public class NavigationApiController extends ApiController {
    @Resource(name = "navigationService")
    private INavigationService iNavigationService;

    /**
     * url: /api/navigations
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "navigations")
    public List<Navigation> getNavigation(HttpServletRequest request) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("permit",true).sort("order",Order.ASC);
        return this.iNavigationService.getAllByParams(null, queryParams);
    }
}
