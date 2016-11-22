package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.MerchantAdv;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IMerchantAdvService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/22.
 */
@Controller
public class RealseMeetController extends SupportController {

    @Resource(name = "merchantAdvService")
    private IMerchantAdvService merchantAdvService;

    @RequestMapping("web/realsemeet")
    public ModelAndView mobileIndexPage(HttpServletRequest request) {   //手机页面
        ModelAndView mv = new ModelAndView("mobile/realsemeet");
        IQueryParams queryParams = createQueryParams(request).sort("advOrder", Order.ASC).sort("createDateTime", Order.DESC);
        List<MerchantAdv> merchantAdvList = this.merchantAdvService.getAllByParams(null, queryParams);
        mv.addObject("merchantAdvList", merchantAdvList);
        return mv;
    }

    @RequestMapping("admin/realsemeet/index")
    public String pcIndexPage() {   //PC后台管理页面
        return "admin/realsemeet/manage";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchantAdv/add", method = RequestMethod.POST)
    public ResponseMessage addMerchantAdv(MerchantAdv merchantAdv) {
        this.merchantAdvService.add(merchantAdv);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchantAdv/list")
    public PageData<MerchantAdv> getMerchantAdvList(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request);
        if (!queryParams.sortContainsKey("advOrder")) {
            queryParams.sort("advOrder", Order.ASC);
        }
        if (!queryParams.sortContainsKey("createDateTime")) {
            queryParams.sort("createDateTime", Order.DESC);
        }
        return this.merchantAdvService.getPageData(createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchantAdv/edit", method = RequestMethod.POST)
    public ResponseMessage editMerchantAdv(MerchantAdv merchantAdv) {
        this.merchantAdvService.update(merchantAdv);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchantAdv/delete", method = RequestMethod.POST)
    public ResponseMessage deleteMerchantAdv(MerchantAdv merchantAdv) {
        this.merchantAdvService.delete(merchantAdv);
        return executeResult();
    }
}
