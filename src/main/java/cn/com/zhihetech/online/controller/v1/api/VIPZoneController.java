package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.service.IVipZoneService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2016/5/17.
 */
@Controller
public class VIPZoneController extends ApiController {

    @Resource(name = "vipZoneService")
    private IVipZoneService vipZoneService;

    @RequestMapping("web/vipzone/index")
    public ModelAndView moboleIndexPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("mobile/vipzone/vipmerchants");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "vipzone/vipmerchants")
    public PageData<Merchant> getVipMerchants(HttpServletRequest request) {
        return this.vipZoneService.getVipMerchants(createPager(request));
    }
}
