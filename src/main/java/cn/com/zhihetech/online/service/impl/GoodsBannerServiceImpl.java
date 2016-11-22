package cn.com.zhihetech.online.service.impl;


import cn.com.zhihetech.online.bean.GoodsBanner;
import cn.com.zhihetech.online.dao.IGoodsBannerDao;
import cn.com.zhihetech.online.service.IGoodsBannerService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/1/7.
 */
@Service(value = "goodsBannerService")
public class GoodsBannerServiceImpl implements IGoodsBannerService {

    @Resource(name = "goodsBannerDao")
   private IGoodsBannerDao goodsBannerDao;

    @Override
    public GoodsBanner getById(String id) {
        return null;
    }

    @Override
    public void delete(GoodsBanner goodsBanner) {

    }

    @Override
    public GoodsBanner add(GoodsBanner goodsBanner) {
        return null;
    }

    @Override
    public void update(GoodsBanner goodsBanner) {

    }

    @Override
    public List<GoodsBanner> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.goodsBannerDao.getEntities(pager,queryParams);
    }

    @Override
    public PageData<GoodsBanner> getPageData(Pager pager, IQueryParams queryParams) {
        return this.goodsBannerDao.getPageData(pager,queryParams);
    }
}
