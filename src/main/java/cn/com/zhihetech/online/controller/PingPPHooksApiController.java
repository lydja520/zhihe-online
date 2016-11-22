package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.commons.ResponseStatusCode;
import cn.com.zhihetech.online.service.ICallbackService;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by YangDaiChun on 2016/1/4.
 */
@Controller
public class PingPPHooksApiController extends SupportController {

    private final static Log log = LogFactory.getLog(PingPPHooksApiController.class);

    @Resource(name = "callbackService")
    private ICallbackService callbackService;

    /**
     * Ping++回调
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "pingPP/hooks")
    public void pingPPWebhooks(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF8");

            //获取头部所有信息
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
            }

            // 获得 http body 内容
            BufferedReader reader = null;
            StringBuffer buffer = null;
            reader = request.getReader();
            buffer = new StringBuffer();
            String string;
            while ((string = reader.readLine()) != null) {
                buffer.append(string);
            }
            reader.close();

            // 解析异步通知数据
            Event event = Webhooks.eventParse(buffer.toString());
            this.callbackService.updateOrderState(event);

            response.setStatus(ResponseStatusCode.SUCCESS_CODE);
        } catch (IOException e) {
            response.setStatus(ResponseStatusCode.SYSTEM_ERROR_CODE);
            log.error("Ping++异步回调失败", e);
        }
    }
}
