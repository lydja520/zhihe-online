package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.FeaturedBlock;
import cn.com.zhihetech.online.controller.QiniuController;
import cn.com.zhihetech.online.dao.IFeaturedBlockDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IFeaturedBlockService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/3/10.
 */
@Service(value = "featuredService")
public class FeaturedBlockServiceImpl implements IFeaturedBlockService {

    @Resource(name = "featuredBlockDao")
    private IFeaturedBlockDao featuredBlockDao;

    @Override
    public FeaturedBlock getById(String id) {
        return null;
    }

    @Override
    public void delete(FeaturedBlock featuredBlock) {

    }

    @Override
    public FeaturedBlock add(FeaturedBlock featuredBlock) {
        return this.featuredBlockDao.saveEntity(featuredBlock);
    }

    @Override
    public void update(FeaturedBlock featuredBlock) {
        this.featuredBlockDao.updateEntity(featuredBlock);
    }

    @Override
    public List<FeaturedBlock> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.featuredBlockDao.getEntities(pager,queryParams);
    }

    @Override
    public PageData<FeaturedBlock> getPageData(Pager pager, IQueryParams queryParams) {
        return this.featuredBlockDao.getPageData(pager, queryParams);
    }

    @Override
    public void updateFbDelState(String fbId, boolean permit) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("fbId", fbId);
        List<Object> permits = this.featuredBlockDao.getProperty("permit", null, queryParams);
        Map<String, Object> paramAndValue = new HashMap<>();
        if (permits.size() <= 0) {
            throw new SystemException("不存在该条记录");
        }
        boolean _permit = (boolean) permits.get(0);
        if (_permit == permit) {
            throw new SystemException("请勿重复操作");
        }
        paramAndValue.put("permit", permit);
        this.featuredBlockDao.executeUpdate(paramAndValue, queryParams);
    }
}
