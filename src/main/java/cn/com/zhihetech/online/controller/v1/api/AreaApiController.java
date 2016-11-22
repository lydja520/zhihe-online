package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IAreaService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/12/16.
 */
@Controller
public class AreaApiController extends ApiController {
    @Resource(name = "areaService")
    private IAreaService areaService;

    @ResponseBody
    @RequestMapping(value = "area/rootAreas")
    public List<Area> getRootAreas(HttpServletRequest request) {
        IQueryParams queryParams = createQueryParams(request).andIsNull("parentArea");
        return this.areaService.getAllByParams(null, queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "area/{parentAreaId}/childAreas")
    public List<Area> getRootAreas(HttpServletRequest request, @PathVariable String parentAreaId) {
        if(StringUtils.isEmpty(parentAreaId)){
            throw new SystemException("父区域ID不能为空");
        }
        IQueryParams queryParams = createQueryParams(request).andNotNull("parentArea").andEqual("parentArea.areaId", parentAreaId);
        return this.areaService.getAllByParams(null, queryParams);
    }
}
