package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.LuckyDrawActivity;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.ILuckyDrawActivityService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by ydc on 16-4-21.
 */
@Controller
public class LuckyDrawActivityController extends SupportController {

    @Resource(name = "luckyDrawActivityService")
    private ILuckyDrawActivityService luckyDrawActivityService;

    @RequestMapping(value = "admim/luckyDrawActivity")
    public String indexPage() {
        return "admin/luckyDraw/luckyDrawActivity";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/luckyDrawActivity/add")
    public ResponseMessage addLuckDrawActivity(LuckyDrawActivity luckyDrawActivity){
        luckyDrawActivity.setCreateDate(new Date());
        luckyDrawActivity.setPermit(false);
        luckyDrawActivity.setSubmitState(false);
        this.luckyDrawActivityService.add(luckyDrawActivity);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/luckyDrawActivity/list")
    public PageData<LuckyDrawActivity> getLuckyDrawActivityPageData(HttpServletRequest request){
        return this.luckyDrawActivityService.getPageData(this.createPager(request),this.createQueryParams(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/ludkyDrawActivity/update")
    public ResponseMessage updateLuckyDrawActivity(LuckyDrawActivity luckyDrawActivity){
        this.luckyDrawActivityService.update(luckyDrawActivity);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/luckyDrawActivity/onlineOrOffline")
    public ResponseMessage executeOnLineHandler(String activityId,boolean permit){
        this.luckyDrawActivityService.executeOnlineOP(activityId,permit);
        return executeResult();
    }
}
