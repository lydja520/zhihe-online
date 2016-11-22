package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Messager;
import cn.com.zhihetech.online.service.IMessagerService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YangDaiChun on 2015/12/18.
 */
@Controller
public class MessagerController  extends  SupportController{

    @Resource(name = "messagerService")
    private IMessagerService messagerService;

    @RequestMapping(value = "admin/messager")
    public String indexPage(){
        return "admin/messager";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/messager/list")
    public PageData<Messager> getPageData(HttpServletRequest request){
            return  this.messagerService.getPageData(this.createPager(request),this.createQueryParams(request));
    }
}
