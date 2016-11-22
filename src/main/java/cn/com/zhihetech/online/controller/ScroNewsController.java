package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.ScroNewsOnApp;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IScroNewOnAppService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YangDaiChun on 2016/8/19.
 */
@Controller
public class ScroNewsController extends SupportController {

    @Resource(name = "scroNewOnAppService")
    private IScroNewOnAppService scroNewOnAppService;

    @RequestMapping(value = "admin/scroNewsOnApp")
    public String scroNewsIndexPage() {
        return "admin/scroNewsOnApp";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/scroNews/add")
    public ResponseMessage addScroNews(ScroNewsOnApp scroNewsOnApp) {
        this.scroNewOnAppService.add(scroNewsOnApp);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/scroNews/edit")
    public ResponseMessage editScroNews(ScroNewsOnApp scroNewsOnApp) {
        this.scroNewOnAppService.update(scroNewsOnApp);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/scroNews/list")
    public PageData<ScroNewsOnApp> getScroeNewsPageData(HttpServletRequest request) {
        return this.scroNewOnAppService.getPageData(this.createPager(request), this.createQueryParams(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/scroNews/del")
    public ResponseMessage deleteScroNewsOnApp(String newId) {
        this.scroNewOnAppService.delete(this.scroNewOnAppService.getById(newId));
        return executeResult();
    }


}
