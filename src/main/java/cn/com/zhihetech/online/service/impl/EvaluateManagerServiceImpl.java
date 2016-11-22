package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.dao.IGoodsDao;
import cn.com.zhihetech.online.dao.IOrderDao;
import cn.com.zhihetech.online.dao.IOrderDetailDao;
import cn.com.zhihetech.online.service.IEvaluateManagerService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by YangDaiChun on 2016/4/5.
 */
@Service(value = "evaluateManagerService")
public class EvaluateManagerServiceImpl implements IEvaluateManagerService{

    @Resource(name = "orderDao")
    private IOrderDao orderDao;
    @Resource(name = "orderDetailDao")
    private IOrderDetailDao orderDetailDao;
    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;

    @Override
    public ModelAndView getOrderEvaluate(String merchantId,ModelAndView mv) {
        return mv;
    }
}
