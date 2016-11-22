package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IAreaService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by YangDaiChun on 2015/11/23.
 */
@Controller
public class AreaController extends SupportController {

    @Resource(name = "areaService")
    private IAreaService areaService;

    @RequestMapping(value = "admin/area")
    public String indexPage() {
        return "admin/area";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/area/list")
    public PageData<Area> getAreaPageData(HttpServletRequest request) {
        IQueryParams queryParams = this.createQueryParams(request);
        return this.areaService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/area/add")
    public ResponseMessage addArea(Area area) {
        if (StringUtils.isEmpty(area.getParentArea().getAreaId())) {
            area.setParentArea(null);
        }
        this.areaService.add(area);
        return this.executeResult();
    }

    private String getAllAreaName(Area area) {
        boolean isRoot = area.getIsRoot();
        while (!isRoot) {
            Area parentArea = this.getAreaById(area.getParentArea().getAreaId());
            this.getAllAreaName(parentArea);

        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/area/edit")
    public ResponseMessage editArea(Area area) {
        this.areaService.update(area);
        return this.executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/area/parentList")
    public List<Area> getParentAreas() {
        return this.areaService.getAllByParams(null, null);
    }

    public Area getAreaById(String id) {
        return this.areaService.getById(id);
    }
}
