package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.GoodsScore;
import cn.com.zhihetech.online.dao.IGoodsDao;
import cn.com.zhihetech.online.dao.IGoodsScoreDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsScoreService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * Created by YangDaiChun on 2016/2/18.
 */
@Service(value = "goodsScoreService")
public class GoodsScoreServiceImpl implements IGoodsScoreService {

    @Resource(name = "goodsScoreDao")
    private IGoodsScoreDao goodsScoreDao;
    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;

    @Override
    public GoodsScore getById(String id) {
        return null;
    }

    @Override
    public void delete(GoodsScore goodsScore) {

    }

    @Override
    public GoodsScore add(GoodsScore goodsScore) {
        return this.goodsScoreDao.saveEntity(goodsScore);
    }

    @Override
    public void update(GoodsScore goodsScore) {

    }

    @Override
    public List<GoodsScore> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }


    @Override
    public PageData<GoodsScore> getPageData(Pager pager, IQueryParams queryParams) {
        PageData<GoodsScore> goodsScorePageData = this.goodsScoreDao.getPageData(pager, queryParams);
        List<GoodsScore> goodsScores = goodsScorePageData.getRows();
        for (GoodsScore goodsScore : goodsScores) {
            goodsScore.setGoods(null);
        }
        return goodsScorePageData;
    }

    @Override
    public PageData<GoodsScore> getAllPageData(Pager pager, IQueryParams queryParams) {
        return this.goodsScoreDao.getPageData(pager, queryParams);
    }

    @Override
    public PageData<GoodsScore> getGoodsScoresByGoodsId(String goodsId, Pager pager) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("goods.goodsId", goodsId).sort("createDate", Order.DESC);
        return this.goodsScoreDao.getPageData(pager, queryParams);
    }

    @Override
    public List<GoodsScore> getGoodsScores(String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("goods.goodsId", goodsId).sort("crateDate", Order.DESC);
        return this.goodsScoreDao.getEntities(queryParams);
    }

    @Override
    public List<GoodsScore> getGoodsScoreBygoodsId(String merchantId, String goodsId, Pager pager, IQueryParams queryParams) {
        IQueryParams _queryParams = new GeneralQueryParams();
        _queryParams.andEqual("merchant.merchantId", merchantId).andEqual("goodsId", goodsId);
        long total = this.goodsDao.getRecordTotal(_queryParams);
        if (total == 0) {
            throw new SystemException("该商品不属于该商家，请检查后重试");
        }
        queryParams.andEqual("goods.goodsId", goodsId);
        return this.goodsScoreDao.getEntities(pager, queryParams);
    }
}
