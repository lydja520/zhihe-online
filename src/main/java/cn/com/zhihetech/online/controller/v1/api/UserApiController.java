package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.Messager;
import cn.com.zhihetech.online.bean.RequestHeader;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.commons.TokenAndUser;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IMessagerService;
import cn.com.zhihetech.online.service.IUserService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.util.common.MD5Utils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/12/16.
 */
@Controller
public class UserApiController extends ApiController {
    @Resource(name = "userService")
    private IUserService userService;
    @Resource(name = "messagerService")
    private IMessagerService messagerService;

    /**
     * 验证注册用户验证码是否正确<br>
     * api/securityCode/verify<br>
     *
     * @param phoneNumber  手机号码
     * @param securityCode 验证码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "securityCode/verify", method = RequestMethod.POST)
    public ResponseMessage verifySecurityCode(String phoneNumber, String securityCode) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("phoneNumber", phoneNumber)
                .andEqual("scState", Constant.SECURITY_REGISTER)
                .andEqual("securityCode", securityCode).andMoreAndEq("validity", new Date());
        List<Messager> messagers = this.messagerService.getAllByParams(null, queryParams);
        if (messagers.size() > 0) {
            return executeResult();
        }
        return executeResult(ResponseStatusCode.SYSTEM_ERROR_CODE, "验证码错误");
    }

    /**
     * <h2>用户注册</h2>
     * URL：api/user/register<br>
     * <p>参数:</p>
     * userPhone  - 用户电话号码<br>
     * userName  - 用户名<br>
     * pwd  - 用户密码<br>
     * area.areaId - 用户所在地区<br>
     * occupation - 用户职业<br>
     * income  - 用户收入<br>
     * sex - 用户性别<br>
     * userBirthday  - 用户生日<br>
     * invitCode  - 用户邀请码<br>
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/register", method = RequestMethod.POST)
    public ResponseMessage userRegister(HttpServletRequest request, User user, String userBirthday) {
        if (user.getArea() != null && StringUtils.isEmpty(user.getArea().getAreaId())) {
            user.setArea(null);
        } else if (user.getArea() != null && user.getArea().getAreaId().length() < 30) {
            //TODO:临时修改，后期需去掉此行代码
            user.getArea().setAreaId("6b001ca8-18c5-40be-9385-9fb388877e07");   //苹果版本注册传递区域ID为区域名称的问题，后期app修复后需更改回来
        }
        user.setBirthday(DateUtils.String2Date(userBirthday));
        this.userService.addUser(user, request);
        return executeResult();
    }


    /**
     * <h2>用户登录</h2>
     * URL：/api/user/login<br>
     *
     * @param userPhone 用户电话号码
     * @param pwd       用户密码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/login", method = RequestMethod.POST)
    public ResponseMessage userLogin(HttpServletRequest request, String userPhone, String pwd) {
        RequestHeader header = new RequestHeader(request);
        TokenAndUser userToken = this.userService.executeUserLogin(userPhone, pwd, header);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "登录成功", userToken);
    }

    /**
     * <h3>修改用户头像</h3>
     * url : api/user/updatePortrait <br>
     * <p>
     * <p>参数</p>
     * userId : 用户Id  <br>
     * imgInfoId ： 用户头像Id <br>
     *
     * @param userId
     * @param imgInfoId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/updatePortrait", method = RequestMethod.POST)
    public ResponseMessage updateUserPortrait(String userId, String imgInfoId) {
        this.userService.updateUserPortrait(userId, imgInfoId);
        return executeResult();
    }

    /**
     * <h3>修改用户信息</h3>
     * url : api/user/updateUserInfo  <br>
     * <p>
     * <p>参数</p>
     * userId : 用户Id <br>
     * userName: 用户名字  <br>
     * sex: 性别 传“true"或“false”true代表男，false代表女   <br>
     * userBirthday : 用户生日  <br>
     * occupation : 用户职业  <br>
     * area.areaId :　用户所属地区 <br>
     * income  : 用户输入  <br>
     *
     * @param user
     * @param userBirthday
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/updateUserInfo", method = RequestMethod.POST)
    public ResponseMessage updateUserInfo(User user, String userBirthday) {
        user.setBirthday(DateUtils.String2Date(userBirthday));
        user = this.userService.updateUser(user);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "用户信息修改成功", user);
    }

    /**
     * <h3>根据用户的id获取用户信息</h3>
     * url: api/user/{id}/userInfo
     * {id}:用户Id
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{id}/userInfo")
    public ResponseMessage getUserInfo(@PathVariable(value = "id") String userId) {
        User user = this.userService.getById(userId);
        return executeResult(ResponseStatusCode.SUCCESS_CODE, "获取用户信息成功", user);
    }
}
