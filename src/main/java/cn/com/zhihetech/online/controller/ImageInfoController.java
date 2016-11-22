package cn.com.zhihetech.online.controller;


import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.commons.ResponseMessage;


import cn.com.zhihetech.online.service.IImgInfoService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by Administrator on 2015/11/26.
 */
@Controller
public class ImageInfoController extends SupportController {
    @Resource(name = "imgInfoService")
    private IImgInfoService imageInfoService;

    @RequestMapping("admin/imgInfo")
    public String imagePage() {
        return "admin/imginfo";
    }

    @ResponseBody
    @RequestMapping("admin/api/imginfo/add")
    public ResponseMessage addImage(ImgInfo imgInfo) {
        imgInfo.setImgInfoId("fsafds");
        imgInfo.setCreateDate(new Date());
        this.imageInfoService.add(imgInfo);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping("admin/api/imginfo/list")
    public PageData<ImgInfo> pageData(HttpServletRequest request) {
        return this.imageInfoService.getPageData(createPager(request), createQueryParams(request));
    }

    @ResponseBody
    @RequestMapping("admin/api/imginfo/remove")
    public ResponseMessage deleteDatas(ImgInfo imgInfo) {
        this.imageInfoService.delete(imgInfo);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping("admin/api/imginfo/deleteBatch")
    public ResponseMessage batchDelete(String imgIds) {
        if (!StringUtils.isEmpty(imgIds)) {
            if (imgIds.endsWith(",")) {
                imgIds = imgIds.substring(0, imgIds.length() - 1);
            }
            this.imageInfoService.deleteBatch(imgIds.split(","));
        }
        return executeResult();
    }
}
