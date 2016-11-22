package cn.com.zhihetech.online.service;

import com.pingplusplus.model.Event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by YangDaiChun on 2016/2/22.
 */
public interface ICallbackService {

    /**
     * Ping++异步回调事件对象
     *
     * @param event
     * @throws IOException
     */
    void updateOrderState(Event event) throws IOException;
}
