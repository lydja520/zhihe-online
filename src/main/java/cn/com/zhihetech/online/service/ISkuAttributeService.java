package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.SkuAttribute;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/2.
 */
public interface ISkuAttributeService extends SupportService<SkuAttribute> {
    /**
     * 根据指定属性ID更新指定的属性
     *
     * @param attrId         属性ID
     * @param attrName       属性名称
     * @param goodsAttrSetId 对应的商品类别ID
     * @param attrDesc       属性描述
     */
    void executeUpdateParamsById(String attrId, String attrName, String goodsAttrSetId, String attrDesc);

    /**
     * 启用或禁用指定的商品属性
     *
     * @param skuAttrId 商品属性ID
     * @param permit    启用或禁用（true:启用；false:禁用）
     */
    void executeUpdatePermit(String skuAttrId, boolean permit);

    List<Object[]> getProperties(String[] skuAttInfo, Pager pager, IQueryParams queryParams);

    List<Object> getProperty(String skuAttName, Pager pager, IQueryParams queryParams);
}
