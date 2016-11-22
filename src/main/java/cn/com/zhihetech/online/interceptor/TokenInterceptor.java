package cn.com.zhihetech.online.interceptor;

import cn.com.zhihetech.online.bean.RequestHeader;
import cn.com.zhihetech.online.bean.TokenInfo;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.controller.v1.api.UserApiController;
import cn.com.zhihetech.util.common.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/22.
 */
public class TokenInterceptor extends UserApiController implements HandlerInterceptor {

    private List<String> unCheckRequest;

    public void setUnCheckRequest(List<String> unCheckRequest) {
        this.unCheckRequest = unCheckRequest;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String requestUri = request.getRequestURI();
        for (String uri : unCheckRequest) {
            if (requestUri.endsWith(uri)) {
                return true;
            }
        }
        return checkAuthorized(request, response);
    }

    private boolean checkAuthorized(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestHeader header = new RequestHeader(request);
        String token = header.getToken();
        if (StringUtils.isEmpty(token)) {
            ResponseMessage responseMessage = new ResponseMessage(ResponseStatusCode.UNAUTHORIZED, false, "Unauthorized!");
            response.setContentType("*/*;charset=UTF-8");
            response.setStatus(ResponseStatusCode.UNAUTHORIZED);
            response.getWriter().write(JSONObject.toJSONString(responseMessage));
            return false;
        }
        TokenInfo tokenInfo = new TokenInfo().decrypt(token);
        if (!tokenInfo.getUserCode().equals(header.getUserCode()) || !tokenInfo.getAppKey().equals(tokenInfo.getAppKey())) {
            ResponseMessage responseMessage = new ResponseMessage(ResponseStatusCode.UNAUTHORIZED, false, "Access not authorized !");
            response.setContentType("*/*;charset=UTF-8");
            response.setStatus(ResponseStatusCode.UNAUTHORIZED);
            response.getWriter().write(JSONObject.toJSONString(responseMessage));
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
