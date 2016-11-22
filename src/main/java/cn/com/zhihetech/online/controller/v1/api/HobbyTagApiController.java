package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.HobbyTag;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.controller.SupportController;
import cn.com.zhihetech.online.service.IHobbyTagService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
public class HobbyTagApiController extends SupportController {
    @Resource(name = "hobbyTagService")
    private IHobbyTagService hobbyTagService;


    @ResponseBody
    @RequestMapping(value = "hobbyTag/rootTags")
    public List<HobbyTag> getRootHobbyTag(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request).andIsNull("parentTag");
        return this.hobbyTagService.getAllByParams(null, queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "hobbyTag/{parentId}/rootTags")
    public List<HobbyTag> getHoobyTagByParent(@PathVariable("parentId") @NotNull String parentId) {
        return this.hobbyTagService.getTagsByParentId(parentId);
    }
}
