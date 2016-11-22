package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.PushBrithdate;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IPushBrithdateService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/3/25.
 */
@Controller
public class PushController extends SupportController {

    @Resource(name = "pushuBrithdateService")
    private IPushBrithdateService pushBrithdateService;

    @ResponseBody
    @RequestMapping(value = "admin/pushMessage/brithdateList")
    public PageData<PushBrithdate> getPushBrithdatePageData(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        String merchantId = this.getCurrentMerchatId(request);
        queryParams.andEqual("focusMerchant.merchant.merchantId",merchantId ).andEqual("pushState", false).sort("nowBrithDay", Order.ASC);
        return this.pushBrithdateService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/pushMessage/brithday")
    public ResponseMessage pushUserbrithdayMsg(String pushBrithdateId, String userId, String pushInfo) {
        this.pushBrithdateService.executePushUserbrithdayMsg(pushBrithdateId, userId, pushInfo);
        return executeResult();
    }
}
