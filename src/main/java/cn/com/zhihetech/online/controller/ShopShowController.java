package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IShopShowService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by ShenYunjie on 2016/1/11.
 */
@Controller
@RequestMapping("admin/")
public class ShopShowController extends SupportController {

    @Resource(name = "shopShowService")
    private IShopShowService shopShowService;

    @RequestMapping("merchant/shopshow")
    public String indexPage() {
        return "admin/merchant/shopShow";
    }

    @ResponseBody
    @RequestMapping(value = "api/shopShow/add",method = RequestMethod.POST)
    public ResponseMessage addShopShow(String merchantId,String... shopShows){
        this.shopShowService.saveOrUpdateShopShow(merchantId,shopShows);
        return executeResult();
    }
}
