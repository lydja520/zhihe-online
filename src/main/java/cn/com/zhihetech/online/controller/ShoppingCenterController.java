package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.ShoppingCenter;
import cn.com.zhihetech.online.commons.ResponseMessage;
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
public class ShoppingCenterController extends SupportController{


    @Resource(name = "shoppingCenterService")
    private IShoppingCenterService shoppingCenterService;


    @RequestMapping(value = "admin/shoppingCenter")
    public String shoppingCenterIndexPg(){
        return "admin/shoppingCenter";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/shoppingCenter/list")
    public PageData<ShoppingCenter> getPageData(HttpServletRequest request){
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("permit",true).sort("scOrder", Order.ASC);
        return this.shoppingCenterService.getPageData(this.createPager(request),queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/shoppingCenter/add")
    public ResponseMessage addShoppingCenter(ShoppingCenter shoppingCenter){
        this.shoppingCenterService.add(shoppingCenter);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/shoppingCenter/edit")
    public ResponseMessage updateShoppingCenter(ShoppingCenter shoppingCenter){
        this.shoppingCenterService.update(shoppingCenter);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/shoppingCenter/updateDelState")
    public ResponseMessage updateShoppingCenterDelState(String scId,boolean permit){
        this.shoppingCenterService.updateDeleteState(scId,permit);
        return executeResult();
    }
}
