package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.HobbyTag;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IHobbyTagService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/1/13.
 */
@Controller
public class HobbyTagController extends SupportController {
    @Resource(name = "hobbyTagService")
    private IHobbyTagService hobbyTagService;

    @RequestMapping(value = "admin/hobbyTag")
    public String indexPage(HttpServletRequest request) {
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/hobbyTag/add", method = RequestMethod.POST)
    public ResponseMessage addHobbyTag(HobbyTag hobbyTag) {
        this.hobbyTagService.add(hobbyTag);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/hobbyTag/rootTags")
    public List<HobbyTag> getRootHobbyTag(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request).andIsNull("parentTag");
        return this.hobbyTagService.getAllByParams(null, queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/hobbyTags")
    public PageData<HobbyTag> getHobbyTagPageData(HttpServletRequest request) {
        return this.hobbyTagService.getPageData(createPager(request), createQueryParams(request));
    }
}
