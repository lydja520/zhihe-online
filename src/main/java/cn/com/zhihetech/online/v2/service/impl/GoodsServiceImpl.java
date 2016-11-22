package cn.com.zhihetech.online.v2.service.impl;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IGoodsDao;
import cn.com.zhihetech.online.service.IGoodsTransientInfoService;
import cn.com.zhihetech.online.v2.service.IGoodsService;
import cn.com.zhihetech.online.vo.GoodsParams;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by ShenYunjie on 2016/7/12.
 */
@Service("v2GoodsService")
public class GoodsServiceImpl implements IGoodsService {

    @Resource(name = "goodsDao")
    protected IGoodsDao goodsDao;
    @Resource(name = "goodsTransientInfoService")
    protected IGoodsTransientInfoService goodsTransientInfoService;

    /**
     * 根据分页参数和查询参数查询商家
     *
     * @param pager
     * @param params
     * @return
     */
    @Override
    public PageData<Goods> getPageDataByParams(Pager pager, GoodsParams params) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("onsale", true)
                .andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK)
                .andEqual("deleteState", false)
                .andEqual("merchant.permit", true)
                .andProParam("stock > 0")
                .andEqual("merchant.examinState", Constant.EXAMINE_STATE_EXAMINED_OK);
        if (params != null) {
            if (!StringUtils.isEmpty(params.getMerchantId())) {
                queryParams.andEqual("merchant.merchantId", params.getMerchantId());
            }
            if (!StringUtils.isEmpty(params.getGoodsAttrSetId())) {
                queryParams.andEqual("goodsAttributeSet.goodsAttSetId", params.getGoodsAttrSetId());
            }
            if (!StringUtils.isEmpty(params.getSearchName())) {
                queryParams.andAllLike(params.getSearchName(), params.getSearchValue());
            }
            if (!StringUtils.isEmpty(params.getSort())) {
                Order order = params.getOrder().toString().toUpperCase().equals(Order.DESC.toString()) ? Order.DESC : Order.ASC;
                queryParams.sort(params.getSort(), order);
            }
        }
        if (!queryParams.sortContainsKey("orderIndex")) {
            queryParams.sort("orderIndex", Order.DESC);
        }
        if (!queryParams.sortContainsKey("onSaleDate")) {
            queryParams.sort("onSaleDate", Order.DESC);
        }
        if (!queryParams.sortContainsKey("createDate")) {
            queryParams.sort("createDate", Order.DESC);
        }
        return this.goodsDao.getPageData(pager, queryParams);
    }
}
