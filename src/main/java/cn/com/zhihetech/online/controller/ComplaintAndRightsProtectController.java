package cn.com.zhihetech.online.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by YangDaiChun on 2016/3/17.
 */
@Controller
public class ComplaintAndRightsProtectController extends SupportController{

    /**
     * <h3>投诉维权</h3>
     * url : web/complaintAndRightProtect  <br>
     *
     * @return
     */
    @RequestMapping(value = "web/complaintAndRightProtect")
    public String complaintAndRightsProtect(){
        return "mobile/complaintAndRightProtect";
    }
}
