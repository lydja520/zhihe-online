package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IBannerService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by Administrator on 2015/12/16.
 */
@Controller
public class BannerController extends SupportController {
    @Resource(name = "bannerService")
    private IBannerService bannerService;

    @RequestMapping(value = "admin/banner")
    public String bannerPage() {
        return "admin/banner";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/banner/list")
    public PageData<Banner> getPageDate(HttpServletRequest request) {
        return this.bannerService.getPageData(createPager(request), createQueryParams(request));
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/banner/add")
    public ResponseMessage addBanner(Banner banner) {
        if (banner.getBannerType() != Constant.BANNER_ONE || banner.getViewType() != Constant.BANNER_VIEWTYPE_TARGET) {
            banner.setViewTargert(null);
        }
        banner.setCreateDate(new Date());
        this.bannerService.add(banner);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/banner/delete", method = RequestMethod.POST)
    public ResponseMessage deleteBanner(String bannerId, String imgInfoId) {
        this.bannerService.deleteBannerAndImg(bannerId, imgInfoId);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/banner/update")
    public ResponseMessage updateBanner(Banner banner) {
        banner.setCreateDate(new Date());
        this.bannerService.update(banner);
        return executeResult();
    }
}
