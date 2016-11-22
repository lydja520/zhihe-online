package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.LuckyDraw;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.ILuckyDrawService;
import cn.com.zhihetech.online.service.IMerchantService;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ydc on 16-4-22.
 */
@Controller
public class LuckyDrawController extends SupportController {

    @Resource(name = "luckyDrawService")
    private ILuckyDrawService luckyDrawService;
    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    @RequestMapping(value = "admin/lkDrawAct/{id}/luckDraw")
    public ModelAndView indexPage(@PathVariable(value = "id") String lkDrawActId) {
        ModelAndView modelAndView = new ModelAndView("admin/luckyDraw/luckyDraw");
        modelAndView.addObject("lkDrawActId", lkDrawActId);
        this.luckyDrawService.addNotWinLuckDraw(lkDrawActId);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/luckDraw/{id}/list")
    public PageData<LuckyDraw> getLuckDrawPageData(@PathVariable(value = "id") String lkDrawActId, HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("luckyDrawActivity.activityId", lkDrawActId)
                .andEqual("delState", false).sort("ldOrder", Order.ASC);
        return this.luckyDrawService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/lkDrawAct/{id}/currentPercentage")
    public Map<String, Object> getCurrentPercentage(@PathVariable(value = "id") String lkDrawActId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("luckyDrawActivity.activityId", lkDrawActId).andEqual("delState", false);
        double percentage = this.luckyDrawService.getCurrentPercentage("sum(percentage)", null, null, queryParams);
        Map map = new HashMap<>();
        map.put("percentage", percentage);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/luckyDraw/add")
    public ResponseMessage addLuckyDraw(LuckyDraw luckyDraw) {
        if (StringUtils.isEmpty(luckyDraw.getMerchant().getMerchantId())) {
            luckyDraw.setMerchant(null);
        }
        this.luckyDrawService.add(luckyDraw);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/luckyDraw/edit")
    public ResponseMessage updateLuckDraw(LuckyDraw luckyDraw) {
        String merchantId = luckyDraw.getMerchant() == null ? null :
                luckyDraw.getMerchant().getMerchantId() == null ? null : luckyDraw.getMerchant().getMerchantId();
        if (StringUtils.isEmpty(merchantId)) {
            luckyDraw.setMerchant(null);
        }
        this.luckyDrawService.update(luckyDraw);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/luckyDraw/merchant/list")
    public PageData<Merchant> merchantPageData(HttpServletRequest request) {
        return this.merchantService.getPageData(this.createPager(request), this.createQueryParams(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/luckyDrawAct/{id}/isEditable")
    public Map<String, Object> getIsEditable(@PathVariable(value = "id") String ludkyDrawActId) {
        Map map = new HashMap<>();
        boolean submitState = this.luckyDrawService.getLkDrawActSubmitState(ludkyDrawActId);
        if (submitState) {
            map.put("isEditable", false);
        } else {
            map.put("isEditable", true);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/luckyDraw/del")
    public ResponseMessage delLuckDraw(String luckDrawId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("luckyDrawId", luckDrawId);
        Map<String, Object> paramAndValue = new HashMap<String, Object>();
        paramAndValue.put("delState", true);
        this.luckyDrawService.executeUpdate(paramAndValue, queryParams);
        return executeResult();
    }

}
