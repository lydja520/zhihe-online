package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.service.IBannerService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/23.
 */
@Controller
public class BannerApiController extends ApiController {

    @Resource(name = "bannerService")
    private IBannerService bannerService;

    /**
     * 获取轮播图
     * URL: /api/banners
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "banners")
    private List<Banner> getBanners(@RequestParam(value = "bannerType", required = false, defaultValue = Constant.BANNER_ONE + "") int  bannerType, HttpServletRequest request) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("bannerType", bannerType).sort("bannerOrder", Order.ASC);
        return this.bannerService.getAllByParams(null, queryParams);
    }

}
