package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.ActivityCategory;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IActivityCategoryService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/12/4.
 */
@Controller
public class ActivitieCategoryController extends SupportController {

    @Resource(name   = "activityCategoryService")
    private IActivityCategoryService activityCategoryService;

    @RequestMapping(value = "admin/activityCategory")
    public String pageIndex() {
        return "admin/activityCategory";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activityCategory/list")
    public PageData<ActivityCategory> getDataPage(HttpServletRequest request) {
        return this.activityCategoryService.getPageData(createPager(request), createQueryParams(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activityCategory/saveOrUpdate")
    public ResponseMessage getDataPage(ActivityCategory activityCategory) {
        if (StringUtils.isEmpty(activityCategory.getCategId())) {
            activityCategory.setCategId(null);
        }
        activityCategory.setCreateDate(new Date());
        this.activityCategoryService.saveOrUpdate(activityCategory);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/activityCategory/myCategList")
    public List<ActivityCategory> getActivityCategByCurrentAdmin(HttpServletRequest request) {
        return this.activityCategoryService.getCategsByAdmin(getCurrentAdmin(request));
    }
}
