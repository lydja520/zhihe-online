package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.ShoppingCenter;
import cn.com.zhihetech.online.service.IShoppingCenterService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YangDaiChun on 2016/3/10.
 */
@Controller
public class ShoppingCenterApiController extends ApiController{

    @Resource(name = "shoppingCenterService")
    private IShoppingCenterService shoppingCenterService;

    /**
     * <h3>获取购物中心列表</h3>
     * url: api/shoppingCenter/list   <br>
     *
      * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "shoppingCenter/list")
    public PageData<ShoppingCenter> getPageData(HttpServletRequest request){
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("permit",true).sort("scOrder", Order.ASC);
        return this.shoppingCenterService.getPageData(this.createPager(request),queryParams);
    }
}
