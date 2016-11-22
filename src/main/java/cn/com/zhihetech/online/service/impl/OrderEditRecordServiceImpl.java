package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.OrderEditRecord;
import cn.com.zhihetech.online.dao.IOrderEditRecordDao;
import cn.com.zhihetech.online.service.IOrderEditRecordService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/5/12.
 */
@Service(value = "orderEditRecordService")
public class OrderEditRecordServiceImpl implements IOrderEditRecordService{

    @Resource(name = "orderRecordDao")
    private IOrderEditRecordDao orderEditRecordDao;

    @Override
    public OrderEditRecord getById(String id) {
        return null;
    }

    @Override
    public void delete(OrderEditRecord orderEditRecord) {

    }

    @Override
    public OrderEditRecord add(OrderEditRecord orderEditRecord) {
        return null;
    }

    @Override
    public void update(OrderEditRecord orderEditRecord) {

    }

    @Override
    public List<OrderEditRecord> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<OrderEditRecord> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }
}
