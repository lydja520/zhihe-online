package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.*;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IOrderDetailService;
import cn.com.zhihetech.online.util.RealAmountUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/1/5.
 */
@Service(value = "orderDeatilService")
public class OrderDetailServiceImpl implements IOrderDetailService {

    @Resource(name = "orderDetailDao")
    private IOrderDetailDao orderDetailDao;

    @Override
    public OrderDetail getById(String id) {
        return null;
    }

    @Override
    public void delete(OrderDetail orderDetail) {

    }

    @Override
    public OrderDetail add(OrderDetail orderDetail) {
        return null;
    }

    @Override
    public void update(OrderDetail orderDetail) {

    }

    @Override
    public List<OrderDetail> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.orderDetailDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<OrderDetail> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }
}
