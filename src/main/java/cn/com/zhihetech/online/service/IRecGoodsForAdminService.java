package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.RecommendGoodsForAdmin;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ydc on 16-8-15.
 */
public interface IRecGoodsForAdminService extends SupportService<RecommendGoodsForAdmin> {
    long getRecTotal(GeneralQueryParams generalQueryParams);

    List<RecommendGoodsForAdmin> getRecGoodses();

    List<Object> getProperty(String s, Pager pager, GeneralQueryParams queryParams);
}
