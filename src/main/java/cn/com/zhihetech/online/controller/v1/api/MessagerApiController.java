package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.IMessagerService;
import cn.com.zhihetech.online.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by YangDaiChun on 2015/12/18.
 */
@Controller
public class MessagerApiController extends ApiController {

    @Resource(name = "messagerService")
    private IMessagerService messagerService;
    @Resource(name = "userService")
    private IUserService userService;

    @RequestMapping(value = "test")
    public String testMessager() {
        return "admin/test";
    }

    /**
     * 获取验证码<br>
     * <p>
     * 访问URL: api/securityCode/get<br>
     *
     * @param phoneNumber   手机号码
     * @param securityState 验证码状态 ；值为 1 则表明为注册用户，值为 2 则表明是忘记密码时修改密码,3表示钱包提现，不传默认是1
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "securityCode/get", method = RequestMethod.POST)
    public ResponseMessage getSecurityCode(String phoneNumber, Integer securityState) throws Exception {
        if (Constant.SECURITY_REGISTER == securityState) {
            this.messagerService.executeSendRegisterSecurityCode(phoneNumber);
        } else if (Constant.SECURITY_ALTERPWD == securityState) {
            this.messagerService.executeSendAlterPwdSecurityCode(phoneNumber);
        } else if (Constant.SECURITY_WITHDRAW_MONEY == securityState) {
            this.messagerService.executeSendWithDrawMoneySecurityCode(phoneNumber);
        }

        return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取验证码成功", Constant.VALIDITY_ONE_MINUTE);
    }

}
