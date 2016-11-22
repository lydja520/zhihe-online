package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.service.IImgInfoService;
import cn.com.zhihetech.online.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by ShenYunjie on 2015/11/19.
 */
@Controller
public class QiniuController extends SupportController {


    private final String CALLBACK_URL = AppConfig.QiNiuConfig.QINIU_CALLBACK_URL;

    private final String CALLBACK_BODY = AppConfig.QiNiuConfig.QINIU_CALLBACK_BODY;

    @Resource(name = "imgInfoService")
    private IImgInfoService imgInfoService;

    @RequestMapping(value = "qiniu/image/upload")
    public String uploadIndex() {
        return "upload";
    }

    /**
     * 获取token
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "qiniu/api/image/uptoken")
    public void getImageUploadToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Auth auth = Auth.create(AppConfig.QiNiuConfig.AK, AppConfig.QiNiuConfig.SK);
        long expires = 1000 * 60 * 60;
        StringMap policy = new StringMap()
                .putNotEmpty("callbackUrl", CALLBACK_URL)
                .put("mimeLimit", "image/jpeg;image/png;image/gif")
                .putNotEmpty("callbackBody", CALLBACK_BODY);
        String uploadToken = auth.uploadToken(AppConfig.QiNiuConfig.BUCKET, null, expires, policy, true);
        String uptokenJson = "{\"uptoken\": \"" + uploadToken + "\"}";
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(uptokenJson);
    }

    @RequestMapping(value = "qiniu/api/image/callback")
    public void qiniuUploadCallback(ImgInfo imgInfo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Enumeration<String> names = request.getParameterNames();
        String owner = request.getParameter("owner");
        imgInfo.setCreateDate(new Date());
        this.imgInfoService.add(imgInfo);
        String imgInfoJson = JSON.toJSONString(imgInfo);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(imgInfoJson);
    }
}
