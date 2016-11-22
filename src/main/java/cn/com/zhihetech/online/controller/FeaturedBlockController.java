package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.bean.FeaturedBlock;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IAreaService;
import cn.com.zhihetech.online.service.IFeaturedBlockService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YangDaiChun on 2016/3/10.
 */
@Controller
public class FeaturedBlockController extends SupportController{

    @Resource(name = "featuredService")
    private IFeaturedBlockService featuredBlockService;
    @Resource(name = "areaService")
    private IAreaService areaService;

    @RequestMapping(value = "admin/featuredBlock")
    public String featuredBlockIndexPg(){
        return "admin/featuredBlock";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/featuredBlock/list")
    public PageData<FeaturedBlock> getPageData(HttpServletRequest request){
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("permit",true).sort("fbOrder", Order.ASC);
        return this.featuredBlockService.getPageData(this.createPager(request),queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/featuredBlock/add")
    public ResponseMessage addFeaturedBlock(FeaturedBlock featuredBlock){
        this.featuredBlockService.add(featuredBlock);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/featuredBlock/edit")
    public ResponseMessage updateFeaturedBlock(FeaturedBlock featuredBlock){
        this.featuredBlockService.update(featuredBlock);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/featurdeBlock/area/list")
    public PageData<Area> getAreaPageData(HttpServletRequest request){
        return this.areaService.getPageData(this.createPager(request),this.createQueryParams(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/featuredBlock/updateDelState")
    public ResponseMessage updateFbDelState(String fbId,boolean permit){
        this.featuredBlockService.updateFbDelState(fbId,permit);
        return executeResult();
    }

}
