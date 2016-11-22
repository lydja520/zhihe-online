package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.Recommend;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IGoodsDao;
import cn.com.zhihetech.online.dao.IRecommendDao;
import cn.com.zhihetech.online.exception.DataRepeatException;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsTransientInfoService;
import cn.com.zhihetech.online.service.IRecommendService;
import cn.com.zhihetech.online.vo.GoodsTransientInfo;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ShenYunjie on 2016/1/11.
 */
@Service("recommendService")
public class RecommendServiceImpl implements IRecommendService {
    @Resource(name = "recommendDao")
    private IRecommendDao recommendDao;
    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;
    @Resource(name = "goodsTransientInfoService")
    private IGoodsTransientInfoService goodsTransientInfoService;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public Recommend getById(String id) {
        return this.recommendDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param recommend 需要删除的持久化对象
     */
    @Override
    public void delete(Recommend recommend) {
        this.recommendDao.deleteEntity(recommend);
    }

    /**
     * 根据推荐ID删除推荐记录
     *
     * @param entityId
     */
    @Override
    public void deleteRecommendById(String entityId) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("recommendId", entityId);
        this.recommendDao.executeDelete(queryParams);
    }

    @Override
    public void updateOderAndReasonById(String recommendId, int orderIndex, String reason) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderIndex", orderIndex);
        params.put("reason", reason);
        IQueryParams queryParams = new GeneralQueryParams().andEqual("recommendId", recommendId);
        this.recommendDao.executeUpdate(params, queryParams);
    }

    /**
     * 根据商家ID获取商家的推荐商品，如推荐商品不足指定后去总数时则返回商家最新的有效商品
     *
     * @param pager
     * @param merchantId
     * @return ArrayList<Goods>
     */
    @Override
    public List<Goods> getRecommendGoodsByMerchantId(Pager pager, String merchantId) {
        List<Goods> goodses = new ArrayList<>();
        IQueryParams query = new GeneralQueryParams()
                .andEqual("merchant.merchantId", merchantId)
                .andEqual("merchant.permit", true)
                .andEqual("goods.onsale", true)
                .andEqual("goods.deleteState", false)
                .andMoreThan("goods.stock", 0L)
                .sort("orderIndex", Order.ASC);
        List<Recommend> recommends = this.recommendDao.getEntities(pager, query);
        List<Object> recommendIds = new ArrayList<>();
        if (recommends != null && !recommends.isEmpty()) {
            for (Recommend recommend : recommends) {
                Goods goods = recommend.getGoods();
                goodses.add(goods);
                recommendIds.add(goods.getGoodsId());
            }
        }
        if (goodses.size() < pager.getRows()) {
            pager.setRows(pager.getRows() - goodses.size());
            query = new GeneralQueryParams()
                    .andEqual("merchant.merchantId", merchantId)
                    .andEqual("merchant.permit", true)
                    .andEqual("onsale", true)
                    .andEqual("deleteState", false)
                    .andMoreThan("stock", 0L)
                    .sort("createDate", Order.DESC);
            if (!recommendIds.isEmpty()) {
                query.andNotIn("goodsId", recommendIds);
            }
            List<Goods> _goodses = this.goodsDao.getEntities(pager, query);
            for (Goods goods : _goodses) {
                goodses.add(goods);
            }
        }
        for (Goods goods : goodses) {
            goods.setMerchant(null);
        }
        return goodses;
    }

    /**
     * 添加一个对象到数据库
     *
     * @param recommend 需要持久化的对象
     * @return
     */
    @Override
    public Recommend add(Recommend recommend) {
        if (recommend.getCreateDate() == null) {
            recommend.setCreateDate(new Date());
        }
        IQueryParams queryParams = new GeneralQueryParams().andEqual("merchant", recommend.getMerchant()).andEqual("goods", recommend.getGoods());
        List<Object> tmp = this.recommendDao.getProperty("count(*)", null, queryParams);
        if (tmp != null && tmp.size() > 0) {
            long count = (long) tmp.get(0);
            if (count >= Constant.MERCHANT_RECOMMEND_MAX) {
                throw new SystemException("推荐商品不得超过" + Constant.MERCHANT_RECOMMEND_MAX + "件，请删除部分推荐后再试");
            }
        }
        tmp = this.recommendDao.getProperty("recommendId", null, queryParams);
        if (tmp != null && tmp.size() > 0) {
            throw new DataRepeatException("此商品已经推荐过请勿重复推荐");
        }
        return recommendDao.saveEntity(recommend);
    }

    /**
     * 更新一个持久化对象
     *
     * @param recommend
     */
    @Override
    public void update(Recommend recommend) {
        this.recommendDao.updateEntity(recommend);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<Recommend> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.recommendDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<Recommend> getPageData(Pager pager, IQueryParams queryParams) {
        return this.recommendDao.getPageData(pager, queryParams);
    }
}
