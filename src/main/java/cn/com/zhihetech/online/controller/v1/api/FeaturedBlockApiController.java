package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.FeaturedBlock;
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
public class FeaturedBlockApiController extends ApiController{

    @Resource(name = "featuredService")
    private IFeaturedBlockService featuredBlockService;

    /**
     * <h3>获取特色街列表</h3>
     * url: api/featuredBlock/list <br>
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "featuredBlock/list")
    public PageData<FeaturedBlock> getPageDate(HttpServletRequest request){
        IQueryParams queryParams = this.createQueryParams(request);
        queryParams.andEqual("permit",true).sort("fbOrder", Order.ASC);
        return this.featuredBlockService.getPageData(this.createPager(request),queryParams);
    }
}
