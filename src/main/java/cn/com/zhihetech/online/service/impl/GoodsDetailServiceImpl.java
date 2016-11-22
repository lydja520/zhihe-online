package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.GoodsDetail;
import cn.com.zhihetech.online.dao.IGoodsDetailDao;
import cn.com.zhihetech.online.service.IGoodsDetailService;
import cn.com.zhihetech.online.service.IGoodsService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/1/7.
 */
@Service(value = "goodsDetailService")
public class GoodsDetailServiceImpl implements IGoodsDetailService {


    @Resource(name = "goodsDetailDao")
    private IGoodsDetailDao goodsDetailDao;

    @Override
    public GoodsDetail getById(String id) {
        return this.goodsDetailDao.findEntityById(id);
    }

    @Override
    public void delete(GoodsDetail goodsDetail) {

    }

    @Override
    public GoodsDetail add(GoodsDetail goodsDetail) {
        return this.goodsDetailDao.saveEntity(goodsDetail);
    }

    @Override
    public void update(GoodsDetail goodsDetail) {

    }

    @Override
    public List<GoodsDetail> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.goodsDetailDao.getEntities(pager,queryParams);
    }

    @Override
    public PageData<GoodsDetail> getPageData(Pager pager, IQueryParams queryParams) {
        return this.getPageData(pager,queryParams);
    }
}
