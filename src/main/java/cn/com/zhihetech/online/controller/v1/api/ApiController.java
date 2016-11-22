package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.controller.SupportController;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.util.common.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.String;
import java.util.Random;
import java.util.UUID;

/**
 * 移动接口基础类
 * Created by ShenYunjie on 2015/12/14.
 */
@RequestMapping("api/")
public class ApiController extends SupportController {

    public final static String CURRENT_TOKEN = "currentUser";

    /**
     * 获取当前用户的session
     *
     * @param request 用户请求
     */
    protected String getCurrentToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(this.CURRENT_TOKEN) == null) {
            return null;
        }
        return (String) session.getAttribute(this.CURRENT_TOKEN);
    }

    /**
     * 保存token到session中
     *
     * @param request 用户请求
     * @param token   需要设置的管理员
     */
    public void setCurrentToken(HttpServletRequest request, String token) {
        if (StringUtils.isEmpty(token)) {
            throw new SystemException("current loged user not able null");
        }
        HttpSession session = request.getSession();
        session.setAttribute(this.CURRENT_TOKEN, token);
    }

    //创建Random对象
    private static Random random = new Random();


    /**
     * 随机生成登录令牌
     *
     * @return
     */
    protected String getToken(String userId) {
        UUID uuid = UUID.randomUUID();
        StringBuffer token = new StringBuffer(userId);
        token.append("#");
        token.append(uuid);
        return this.getBase64(token.toString());
    }

    /**
     * base64加密
     */
    protected static String getBase64(String str) {
        String _str = null;
        try {
            _str = new BASE64Encoder().encode(str.getBytes(Constant.ENCODING));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return _str.replaceAll("\\r\\n", "");
    }

    /**
     * base64解密
     */
    protected static String getFromBase64(String str) {
        String _str = null;
        try {
            _str = String.valueOf(new BASE64Decoder().decodeBuffer(str));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _str;
    }

    /**
     * 生成指定位数的随机数
     */
    protected static String getNumber(int z) {
        String result = "";
        Random random = new Random();
        for (int i = 0; i < z; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

    protected String get5RandomNum() {
        int num = 0;
        while (true) {
            num = (int) (Math.random() * 100000);
            if (num > 10000 & num < 100000) {
                break;
            }
        }
        return num + "";
    }

    @Override
    protected ResponseMessage executeResult() {
        return this.executeResult("操作成功");
    }

    @Override
    protected ResponseMessage executeResult(String msg) {
        return executeResult(ResponseStatusCode.SUCCESS_CODE, msg);
    }

    @Override
    protected ResponseMessage executeResult(int code, String msg) {
        if (code == ResponseStatusCode.SYSTEM_ERROR_CODE || code == ResponseStatusCode.UNAUTHORIZED || code == ResponseStatusCode.PAGE_NOT_FOUND_CODE) {
            return executeResult(code, false, msg);
        }
        return executeResult(code, true, msg);
    }

    /**
     * 异常处理
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler
    @Override
    public ResponseMessage handlerException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error(ex.getMessage(), ex);
        response.setStatus(200);
        /*if (ex instanceof SystemRuntimeException) {
            return executeResult(500, ex.getMessage());
        } else {
            return executeResult(500, "系统内部错误");
        }*/
        return executeResult(500, ex.getMessage());
    }
}
