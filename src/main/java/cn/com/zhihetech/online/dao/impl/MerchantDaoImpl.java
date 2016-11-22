package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IMerchantDao;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by ShenYunjie on 2015/11/20.
 */
@Repository("merchantDao")
public class MerchantDaoImpl extends SimpleSupportDao<Merchant> implements IMerchantDao {

    /**
     * 根据商品类别获取商家
     *
     * @param goodsAttSetId
     * @param pager
     * @return
     */
    @Override
    public PageData<Merchant> getMerchantsByGoodsAttSet(String goodsAttSetId, Pager pager) {

        Map<String, Object> params = new HashMap<>();
        params.put("attSetId", goodsAttSetId);
        params.put("permit", true);
        params.put("examinState", Constant.EXAMINE_STATE_EXAMINED_OK);
        StringBuilder hql = new StringBuilder("from ").append(getTableName()).append(" as m join m.categories as ct")
                .append(" where ct.goodsAttSetId = :attSetId and m.permit = :permit and m.examinState = :examinState")
                .append(" order by m.merchOrder desc,m.updateDate desc,m.createDate desc");

        long total = this.getRecordTotal("select count(distinct m.merchantId) " + hql.toString(), params);

        Query query = createQueryWithHQL("select distinct m " + hql.toString());
        initQueryValues(query, params);
        initQueryPage(query, pager);

        List<Merchant> merchants = query.list();

        PageData<Merchant> pageData = new PageData<Merchant>(total, pager).setRows(merchants);

        return pageData;
    }
}
