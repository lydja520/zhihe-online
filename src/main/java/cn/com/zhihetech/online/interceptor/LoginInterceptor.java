package cn.com.zhihetech.online.interceptor;

import cn.com.zhihetech.online.controller.SupportController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/1.
 */
public class LoginInterceptor extends SupportController implements HandlerInterceptor {

    private List<String> unCheckRequest;

    public List<String> getUnCheckRequest() {
        return unCheckRequest;
    }

    public void setUnCheckRequest(List<String> unCheckRequest) {
        this.unCheckRequest = unCheckRequest;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String requestUri = httpServletRequest.getRequestURI();
        for (String uri : unCheckRequest) {
            if (requestUri.endsWith(uri)) {
                return true;
            }
        }
        if (this.getCurrentAdmin(httpServletRequest) != null) {
            return true;
        } else {
            String contextPath = httpServletRequest.getContextPath();
            httpServletResponse.sendRedirect(contextPath + "/admin/login");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
