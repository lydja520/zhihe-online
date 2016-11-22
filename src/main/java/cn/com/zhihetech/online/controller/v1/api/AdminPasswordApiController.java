package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.service.IAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by YangDaiChun on 2016/3/9.
 */
@Controller
public class AdminPasswordApiController extends ApiController{

    @Resource(name = "adminService")
    private IAdminService adminService;

    /**
     *<h3>修改商家密码</h3>
     *url : api/admin/changePwd   <br>
     *
     * <p>参数</p>
     *  adminCode : 商家账号 <br>
     *  oldPwd : 旧密码  <br>
     *  newPwd : 新密码  <br>
     *
     * @param adminCode
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "admin/changePwd",method = RequestMethod.POST)
    public ResponseMessage changeMerchantPwd(String adminCode,String oldPwd,String newPwd){
        this.adminService.changePassword(adminCode,oldPwd,newPwd);
        return executeResult();
    }
}
