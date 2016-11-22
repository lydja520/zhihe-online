package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.Messager;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IMessagerService;
import cn.com.zhihetech.online.service.IUserService;
import cn.com.zhihetech.util.common.MD5Utils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/3/9.
 */
@Controller
public class UserPasswordApiController extends ApiController {

    @Resource(name = "userService")
    private IUserService userService;
    @Resource(name = "messagerService")
    private IMessagerService messagerService;

    /**
     * <h3>修改用户密码</h3>
     * url : api/user/changePwd <br>
     * <p>
     * <p>参数</p>
     * userId : 用户Id <br>
     * oldPwd : 用户旧密码  <br>
     * newPwd : 用户新密码  <br>
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/changePwd", method = RequestMethod.POST)
    public ResponseMessage changePwd(String userId, String oldPwd, String newPwd) {
        this.userService.updatePwd(userId, oldPwd, newPwd);
        return executeResult();
    }

    /**
     * <h3>用户忘记密码，短信方式发送6位随机密码，并重置为新的6位密码</h3>
     * url : api/user/resetPwd   <br>
     * <p>
     * <p>参数：</p>
     * phoneNumber：电话号码   <br>
     * securityCode：验证码  </br>
     *
     * @param phoneNumber
     * @param securityCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/resetPwd")
    public ResponseMessage resetPassword(String phoneNumber, String securityCode) {
        this.messagerService.resetPassword(phoneNumber, securityCode);
        return executeResult();
    }
}
