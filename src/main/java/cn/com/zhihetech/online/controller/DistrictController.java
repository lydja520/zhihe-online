package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.District;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IDistrictService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2015/11/20.
 */
@Controller
public class DistrictController extends SupportController {
    @Resource(name = "districtService")
    private IDistrictService districtService;

    @RequestMapping(value = "admin/district")
    public String indexPage(){
        return "admin/district";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/district/list")
    public PageData<District> getDistrictPageData(HttpServletRequest request) {
        return this.districtService.getPageData(createPager(request), createQueryParams(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/district/add", method = RequestMethod.POST)
    public ResponseMessage addDistrict(District district) {
        this.districtService.add(district);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/district/edit")
    public ResponseMessage editDistrict(District district){
        this.districtService.update(district);
        return executeResult();
    }
}
