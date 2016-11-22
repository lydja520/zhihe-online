package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.GoodsAttribute;
import cn.com.zhihetech.online.vo.GoodsAttrInfo;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ydc on 16-7-6.
 */
public interface IGoodsAttributeService extends SupportService<GoodsAttribute> {
    /**
     * 根据商品ID获取此商品的属性信息
     *
     * @param goodsId
     * @return
     */
    List<GoodsAttrInfo> getGoodsAttrInfosByGoodsId(String goodsId);


    /**
     * 获取指定的单个属性值
     *
     * @param s
     * @param pager
     * @param queryParams
     * @return
     */
    List<Object> getProperty(String s, Pager pager, IQueryParams queryParams);

    /**
     * 获取指定的一组属性值
     *
     * @param selectors
     * @param pager
     * @param queryParams
     * @return
     */
    List<Object[]> getProperties(String[] selectors, Pager pager, IQueryParams queryParams);

    /**
     * 根据商品ID获取商品属性与商品属性值的组合数组。形式为["attrId:attrValue","attrId:attrValue"]
     *
     * @param skuId
     * @return
     */
    List<String> getAttrAndValueArrayBySkuId(String skuId);

    long getRecordTotal(IQueryParams queryParams);

    /**
     * 更新商品与商品属性的组合
     *
     * @param goodsAttr
     */
    void executeUpdate(GoodsAttribute goodsAttr);

    /**
     * 根据skuId获取商品与商品属性的组合总数
     *
     * @param skuId 商品属性组合（SKU)ID
     * @return
     */
    int getGoodsAttrCountBySkuId(String skuId);

    String getSkuNameBySkuId(String skuId);
}
