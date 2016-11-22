package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.MerchantBillErrRecord;
import cn.com.zhihetech.online.dao.IMerchantBillErrRecordDao;
import cn.com.zhihetech.online.service.IMerchantBillErrRecordService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/5/18.
 */
@Service(value = "merchantBillErrRecordService")
public class MerchantBillErrServiceImpl implements IMerchantBillErrRecordService {

    @Resource(name = "merchantBilErrRecordDao")
    private IMerchantBillErrRecordDao merchantBillErrRecordDao;

    @Override
    public MerchantBillErrRecord getById(String id) {
        return null;
    }

    @Override
    public void delete(MerchantBillErrRecord merchantBillErrRecord) {

    }

    @Override
    public MerchantBillErrRecord add(MerchantBillErrRecord merchantBillErrRecord) {
        return null;
    }

    @Override
    public void update(MerchantBillErrRecord merchantBillErrRecord) {

    }

    @Override
    public List<MerchantBillErrRecord> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<MerchantBillErrRecord> getPageData(Pager pager, IQueryParams queryParams) {
        return this.merchantBillErrRecordDao.getPageData(pager, queryParams);
    }
}
