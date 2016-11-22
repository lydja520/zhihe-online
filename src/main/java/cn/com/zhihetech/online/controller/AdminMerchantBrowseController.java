package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.MerchantBrowse;
import cn.com.zhihetech.online.service.IMerchantBrowseService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/6/7.
 */
@Controller
public class AdminMerchantBrowseController extends SupportController {
    @Resource
    private IMerchantBrowseService merchantBrowseService;

    @RequestMapping(value = "admin/adminMerchantBrowse")
    public String adminMerchantBrowse(HttpServletRequest request){
        if(this.getCurrentMerchatId(request)!=null){
            return "admin/currentMerchantBrowse";
        }
        return "admin/adminMerchantBrowse";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchantbrowse/list")
    public PageData<MerchantBrowse> getMerchantBrowse(HttpServletRequest request,String initTime,String endTime,String userName){
        IQueryParams queryParams = this.createQueryParams(request);
        String merchantId = this.getCurrentMerchatId(request);
        if(!StringUtils.isEmpty(userName)){
            queryParams.andAllLike("user.userName",userName);
        }
        if(!StringUtils.isEmpty(merchantId)){
            queryParams.andEqual("merchant.merchantId",merchantId);
        }
        if(!StringUtils.isEmpty(initTime)){
            queryParams.andMoreAndEq("browseDate",DateUtils.getStartDateTimeWithDate(DateUtils.String2Date(initTime)));
        }
        if(!StringUtils.isEmpty(endTime)){
            queryParams.andLessAndEq("browseDate",DateUtils.getEndDateTimeWithDate(DateUtils.String2Date(endTime)));
        }
        if(request.getParameter("sort")==null){
            queryParams.sort("browseDate", Order.DESC);
        }
        PageData<MerchantBrowse> merchantBrowsePageData = this.merchantBrowseService.getPageData(this.createPager(request),queryParams);
        return merchantBrowsePageData;
    }
}
