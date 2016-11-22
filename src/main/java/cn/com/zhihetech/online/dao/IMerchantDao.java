package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.MerchantAndCategory;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

import java.util.Map;

/**
 * Created by ShenYunjie on 2015/11/20.
 */
public interface IMerchantDao extends SupportDao<Merchant> {

    /**
     * 根据商品类别获取商家
     *
     * @param goodsAttSetId
     * @param pager
     * @return
     */
    PageData<Merchant> getMerchantsByGoodsAttSet(String goodsAttSetId, Pager pager);
}
