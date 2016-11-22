package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.AppHomeImg;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IAppHomeImgService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
@Controller
public class AppHomeImgController extends SupportController {

    @Resource(name = "appHomeImgService")
    private IAppHomeImgService appHomeImgService;

    @RequestMapping(value = "admin/appHomeImg")
    public String indexPage() {
        return "admin/appHomeImg";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/appHomeImg/list")
    public PageData<AppHomeImg> getAppHomeImgs(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        return this.appHomeImgService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/appHomeImg/add")
    public ResponseMessage addOrUpdate(AppHomeImg appHomeImg) {
        this.appHomeImgService.addOrUpdateImg(appHomeImg);
        return executeResult();
    }
}
