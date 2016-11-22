package cn.com.zhihetech.online.interceptor;

import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.controller.SupportController;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by YangDaiChun on 2015/12/1.
 */
public class ApiIntercepter extends SupportController implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (this.getCurrentAdmin(httpServletRequest) == null) {
            httpServletResponse.setContentType("*/*;charset=UTF-8");
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setCode(ResponseStatusCode.UNAUTHORIZED);
            responseMessage.setMsg("你还未登录，请登录后重试!");
            responseMessage.setSuccess(false);
            httpServletResponse.setStatus(ResponseStatusCode.UNAUTHORIZED);
            String json = JSONObject.toJSONString(responseMessage);
            httpServletResponse.getWriter().write(json);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
