package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.MerchantScore;
import cn.com.zhihetech.online.dao.IMerchantScoreDao;
import cn.com.zhihetech.online.service.IMerchantScoreService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/2/18.
 */
@Service(value = "merchantScoreService")
public class MerchantScoreServiceImpl implements IMerchantScoreService {

    @Resource(name = "merchantScoreDao")
    private IMerchantScoreDao merchantScoreDao;

    @Override
    public MerchantScore getById(String id) {
        return null;
    }

    @Override
    public void delete(MerchantScore merchantScore) {

    }

    @Override
    public MerchantScore add(MerchantScore merchantScore) {
        return null;
    }

    @Override
    public void update(MerchantScore merchantScore) {

    }

    @Override
    public List<MerchantScore> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<MerchantScore> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public Float getMerchantScoreByMerchantId(String merchantId) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("merchant.merchantId", merchantId);
        List<Object> objs = this.merchantScoreDao.getProperty("merScoreId", null, queryParams);
        if (objs.size() < 1) {
            return 5f;
        }
        objs = this.merchantScoreDao.getProperty("sum(score)/count(merScoreId)", null, queryParams);
        return Float.parseFloat(objs.get(0).toString());
    }
}
