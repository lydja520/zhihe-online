package cn.com.zhihetech.online.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by YangDaiChun on 2016/5/20.
 */
@Controller
public class appDownloadController extends SupportController{

    @RequestMapping(value = "downloadApp")
    public String downloadApp(){
        return "downloadApp";
    }
}
