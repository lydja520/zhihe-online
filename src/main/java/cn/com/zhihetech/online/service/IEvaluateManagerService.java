package cn.com.zhihetech.online.service;

import org.springframework.web.servlet.ModelAndView;

import java.awt.event.MouseEvent;

/**
 * Created by YangDaiChun on 2016/4/5.
 */
public interface IEvaluateManagerService {

    ModelAndView getOrderEvaluate(String merchantId,ModelAndView mv);
}
