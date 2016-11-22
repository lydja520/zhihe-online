package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.Sku;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IGoodsDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.vo.GoodsParams;
import cn.com.zhihetech.online.vo.GoodsTransientInfo;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2015/12/14.
 */
@Repository(value = "goodsDao")
public class GoodsDaoImpl extends SimpleSupportDao<Goods> implements IGoodsDao {

    /**
     * 批量更新商品的销量
     *
     * @param goodsIds    商品ID列表
     * @param goodsCounts 商品购买购买的数量列表
     */
    @Override
    public void executeAddVolumeByGoodsIdsAndGoodsCounts(List<String> goodsIds, List<Long> goodsCounts) {
        if (goodsIds == null || goodsCounts.isEmpty()) {
            throw new SystemException("goods id list is not able null or empty");
        }
        if (goodsCounts == null || goodsIds.size() != goodsCounts.size()) {
            throw new SystemException("goods id list size not equal goods count size");
        }
        StringBuffer hql = new StringBuffer("update ").append(getTableName())
                .append(" set volume = (volume + :saleCount1)")
                .append(" where goodsId = :goodsId and deleteState = false and examinState = :examineOk and onsale = true")
                .append(" and stock >= (volume + :saleCount2)");
        Map<String, Object> params = new HashMap<>();
        Query query = createQueryWithHQL(hql.toString());
        for (int i = 0; i < goodsIds.size(); i++) {
            params.clear();
            String goodsId = goodsIds.get(i);
            Long saleCount = goodsCounts.get(i);
            if (saleCount <= 0) {
                throw new SystemException("购买的商品数量不能小于或等于零！");
            }
            params.put("saleCount1", saleCount);
            params.put("goodsId", goodsId);
            params.put("examineOk", Constant.EXAMINE_STATE_EXAMINED_OK);
            params.put("saleCount2", saleCount);
            initQueryValues(query, params);
            if (query.executeUpdate() < 1) {
                throw new SystemException("商品库存不足！");
            }
        }
    }

    /**
     * 减少商品的销量
     *
     * @param goodsId
     * @param goodsCount
     */
    @Override
    public void executeSubGoodsVolume(String goodsId, long goodsCount) {
        if (goodsCount <= 0) {
            throw new SystemException("购买的商品数量不能小于或等于零");
        }
        String sql = "UPDATE t_goods SET goods_volume=goods_volume-:goodsCount WHERE goods_id =:goodsId";
        Query query = this.getSession().createSQLQuery(sql);
        query.setParameter("goodsCount", goodsCount);
        query.setParameter("goodsId", goodsId);
        query.executeUpdate();
    }

    /**
     * 增加商品的销量
     */
    @Override
    public void executeAddGoodsVolume(String goodsId, long volume) {
        String sql = "UPDATE t_goods SET goods_volume=goods_volume+:goodsCount WHERE goods_id =:goodsId";
        Query query = this.getSession().createSQLQuery(sql);
        query.setParameter("goodsCount", volume);
        query.setParameter("goodsId", goodsId);
        query.executeUpdate();
    }

    /**
     * 根据请求参数获取商品列表,返回的商品需要调用setTransientInfo(...)方法
     *
     * @param page   分页
     * @param params 查询参数
     * @return
     */
    @Override
    public PageData<Goods> getGoodsPageDataByParams(Pager page, GoodsParams params) {

        String querySql = getQuerySql(params);
        String sortSql = getSortSql(params);

        Object obj = getSession().createSQLQuery("SELECT COUNT(g.goods_id) " + querySql).uniqueResult();
        Long total = Long.parseLong(obj.toString());

        PageData<Goods> pageData = new PageData<>(total, page);

        Query query = getSession().createSQLQuery("SELECT g.goods_id " + querySql + sortSql);
        List<String> ids = query.list();

        if (ids.isEmpty()) {
            return pageData;
        }

        List<Goods> rows = new ArrayList<>();
        for (String id : ids) {
            rows.add(this.findEntityById(id));
        }
        pageData.setRows(rows);

        return pageData;
    }

    /**
     * 获取查询语句（不包括排序）
     *
     * @param params
     * @return
     */
    protected String getQuerySql(GoodsParams params) {
        final String table = "SELECT goods_id,MIN(price) AS price, MIN(price) AS minPrice, MAX(price) AS maxPrice, SUM(stock) AS stock, SUM(volume) AS volume " +
                "FROM t_sku GROUP BY goods_id";
        final StringBuffer querySql = new StringBuffer("FROM t_goods AS g,")
                .append("(").append(table).append(") AS t")
                .append(",t_merchant m")
                .append(" WHERE g.goods_id = t.goods_id")
                .append(" AND g.onsale = true AND g.examin_state = ").append(Constant.EXAMINE_STATE_EXAMINED_OK)
                .append(" AND g.merch_id = m.merch_id")
                .append(" AND m.permit = true")
                .append(" AND m.examin_state = ").append(Constant.EXAMINE_STATE_EXAMINED_OK)
                .append(" AND deleteState = false");
        if (params != null) {
            if (!StringUtils.isEmpty(params.getMerchantId())) {
                querySql.append(" AND g.merch_id = '").append(params.getMerchantId()).append("'");
            }
            if (!StringUtils.isEmpty(params.getGoodsAttrSetId())) {
                querySql.append(" AND g.goods_att_set = '").append(params.getGoodsAttrSetId()).append("'");
            }
            if (!StringUtils.isEmpty(params.getSearchValue())) {
                querySql.append(" AND g.goods_name LIKE '%").append(params.getSearchValue()).append("%'");
            }
        }
        return querySql.toString();
    }

    /**
     * 获取排序sql语句
     *
     * @param params
     * @return
     */
    protected String getSortSql(GoodsParams params) {
        StringBuffer sortSql = new StringBuffer(" ORDER BY");
        boolean flag = false;
        if (params != null && !StringUtils.isEmpty(params.getSort())) {
            switch (params.getSort()) {
                case GoodsParams.SORT_CREATE_DATE:
                    sortSql.append(" g.create_date ").append(params.getOrder().toUpperCase());
                    flag = true;
                    break;
                case GoodsParams.SORT_ON_SALE_DATE:
                    sortSql.append(" g.on_sale_date ").append(params.getOrder().toUpperCase());
                    flag = true;
                    break;
                case GoodsParams.SORT_PRICE:
                    sortSql.append(" t.price ").append(params.getOrder().toUpperCase());
                    flag = true;
                    break;
                case GoodsParams.SORT_VOLUME:
                    sortSql.append(" t.volume ").append(params.getOrder().toUpperCase());
                    flag = true;
                    break;
            }
        }
        sortSql.append(flag ? ",g.order_index " : " g.order_index ").append(Order.DESC)
                .append(",g.on_sale_date ").append(Order.DESC)
                .append(",g.create_date ").append(Order.DESC);
        return sortSql.toString();
    }
}
