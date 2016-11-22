package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.RecommendMerchantForAdmin;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ydc on 16-8-15.
 */
public interface IRecMerchantForAdminService extends SupportService<RecommendMerchantForAdmin> {
    List<RecommendMerchantForAdmin> getRecMerchants();

    long getRecTotal(GeneralQueryParams generalQueryParams);

    List<Object> getProperty(String selectror,Pager pager, IQueryParams queryParams);
}
