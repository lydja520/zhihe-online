package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Sku;
import cn.com.zhihetech.online.dao.ISkuDao;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/2.
 */
@Repository("skuDao")
public class SkuDaoImpl extends SimpleSupportDao<Sku> implements ISkuDao {

    @Override
    public Sku getMaxStockSkuByGoodsId(String goodsId) {
        String hql = "FROM " + getTableName() + " WHERE goodsId = :goodsId ORDER BY (stock - volume) DESC";
        Query query = createQueryWithHQL(hql.toString());
        query.setParameter("goodsId", goodsId);
        query.setFirstResult(0);
        query.setMaxResults(1);
        List<Sku> skuList = query.list();
        return skuList.isEmpty() ? null : skuList.get(0);
    }

    @Override
    public int addSkuVolumeBySkuAndCount(String skuId, Long count) {
        StringBuilder hql = new StringBuilder("UPDATE ").append(getTableName())
                .append(" SET volume = volume + :count WHERE skuId = :skuId AND (stock - volume) >= :count1 ");
        Query query = createQueryWithHQL(hql.toString());
        query.setString("skuId",skuId);
        query.setParameter("count", count);
        query.setParameter("count1", count);
        return query.executeUpdate();
    }

    @Override
    public int subSkuVolumeBySkuAndCount(String skuId, Long count) {
        StringBuilder hql = new StringBuilder("UPDATE ").append(getTableName())
                .append(" SET volume = volume-:count WHERE skuId = :skuId AND volume >= :count1");
        Query query = createQueryWithHQL(hql.toString());
        query.setString("skuId",skuId);
        query.setParameter("count", count);
        query.setParameter("count1", count);
        return query.executeUpdate();
    }
}
