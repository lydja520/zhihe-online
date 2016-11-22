package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.RequestHeader;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.commons.TokenAndMerchant;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.util.common.MD5Utils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/2/29.
 */
@Controller
public class MerchantLoginApiController extends ApiController {

    @Resource(name = "adminService")
    IAdminService adminService;

    /**
     * <h3>商家登录</h3>
     * url: api/merchant/login <br>
     * <p>
     * <p>参数</p>
     * adminCode: 商家账号  <br>
     * adminPwd: 商家密码  <br>
     *
     * @param adminCode
     * @param adminPwd
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "merchant/login", method = RequestMethod.POST)
    public ResponseMessage merchantLogin(String adminCode, String adminPwd, HttpServletRequest request) {
        RequestHeader header = new RequestHeader(request);
        TokenAndMerchant tokenAndMerchant = this.adminService.executeAppLogin(adminCode,adminPwd,header);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "登录成功", tokenAndMerchant);
    }

}
