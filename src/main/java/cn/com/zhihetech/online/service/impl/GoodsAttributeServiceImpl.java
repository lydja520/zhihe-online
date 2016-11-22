package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.GoodsAttribute;
import cn.com.zhihetech.online.dao.IGoodsAttributeDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsAttributeService;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.online.vo.GoodsAttrInfo;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ydc on 16-7-6.
 */
@Service(value = "goodsAttributeService")
public class GoodsAttributeServiceImpl implements IGoodsAttributeService {

    @Resource(name = "goodsAttributeDao")
    private IGoodsAttributeDao goodsAttributeDao;

    @Override
    public GoodsAttribute getById(String id) {
        return null;
    }

    @Override
    public void delete(GoodsAttribute goodsAttribute) {

    }

    @Override
    public GoodsAttribute add(GoodsAttribute goodsAttribute) {
        if (StringUtils.isEmpty(goodsAttribute.getAttrValue())) {
            throw new SystemException("商品属性值不能为空！");
        }
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("attribute.skuAttId", goodsAttribute.getAttribute().getSkuAttId())
                .andEqual("sku.skuId", goodsAttribute.getSku().getSkuId());
        List<Object> objects = this.goodsAttributeDao.getProperty("goodsAttrId", null, queryParams);
        if (objects != null && !objects.isEmpty()) {
            throw new SystemException("属性组合中的商品属性重复！");
        }
        return this.goodsAttributeDao.saveEntity(goodsAttribute);
    }

    @Override
    public void update(GoodsAttribute goodsAttribute) {
        this.goodsAttributeDao.updateEntity(goodsAttribute);
    }

    @Override
    public List<GoodsAttribute> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<GoodsAttribute> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public List<GoodsAttrInfo> getGoodsAttrInfosByGoodsId(String goodsId) {
        new GoodsAttribute();
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goods.goodsId", goodsId)
                .sort("attribute.skuAttId", Order.ASC);
        List<Object> objects = this.goodsAttributeDao.getProperty("attribute.skuAttId", null, queryParams);
        List<String> attrIds = new ArrayList<>();
        for (Object obj : objects) {
            if (!attrIds.contains(obj)) {
                attrIds.add(obj.toString());
            }
        }
        List<GoodsAttrInfo> goodsAttrInfos = new ArrayList<>();
        String[] values = new String[]{"attribute.skuAttId", "attribute.skuAttName", "attrValue"};
        for (String attrId : attrIds) {
            queryParams = new GeneralQueryParams()
                    .andEqual("goods.goodsId", goodsId)
                    .andEqual("attribute.skuAttId", attrId);
            List<Object[]> result = this.goodsAttributeDao.getProperties(values, null, queryParams);
            if (result.isEmpty()) {
                continue;
            }
            GoodsAttrInfo goodsAttrInfo = new GoodsAttrInfo();
            goodsAttrInfo.setAttrId(result.get(0)[0].toString());
            goodsAttrInfo.setAttrName(result.get(0)[1].toString());
            goodsAttrInfo.setGoodsId(goodsId);
            List<String> tags = new ArrayList<>();
            for (Object[] objs : result) {
                if (tags.contains(objs[2])) {
                    continue;
                }
                tags.add(objs[2].toString());
            }
            goodsAttrInfo.setTags(tags);
            goodsAttrInfos.add(goodsAttrInfo);
        }
        return goodsAttrInfos;
    }

    /**
     * 获取指定的单个属性值
     *
     * @param s
     * @param pager
     * @param queryParams
     * @return
     */
    @Override
    public List<Object> getProperty(String s, Pager pager, IQueryParams queryParams) {
        return this.goodsAttributeDao.getProperty(s, pager, queryParams);
    }

    /**
     * 获取指定的一组属性值
     *
     * @param selectors
     * @param pager
     * @param queryParams
     * @return
     */
    @Override
    public List<Object[]> getProperties(String[] selectors, Pager pager, IQueryParams queryParams) {
        return this.goodsAttributeDao.getProperties(selectors, pager, queryParams);
    }

    /**
     * 根据商品ID获取商品属性与商品属性值的组合数组。形式为["attrId:attrValue","attrId:attrValue"]
     *
     * @param skuId
     * @return
     */
    public List<String> getAttrAndValueArrayBySkuId(String skuId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("sku.skuId", skuId)
                .sort("attribute.skuAttId", Order.ASC);
        String[] values = new String[]{"attribute.skuAttId", "attrValue"};
        List<Object[]> tmps = this.goodsAttributeDao.getProperties(values, null, queryParams);
        List<String> attrIdAndValueCodes = new ArrayList<>();
        for (Object[] array : tmps) {
            String code = array[0].toString().trim() + ":" + array[1].toString().trim();
            if (!attrIdAndValueCodes.contains(code)) {
                attrIdAndValueCodes.add(code);
            }
        }
        return attrIdAndValueCodes;
    }

    @Override
    public long getRecordTotal(IQueryParams queryParams) {
        return this.goodsAttributeDao.getRecordTotal(queryParams);
    }

    @Override
    public void executeUpdate(GoodsAttribute goodsAttr) {
        if (StringUtils.isEmpty(goodsAttr.getAttrValue())) {
            throw new SystemException("商品属性值不能为空！");
        }
        this.goodsAttributeDao.updateEntity(goodsAttr);
    }

    /**
     * 根据skuId获取商品与商品属性的组合总数
     *
     * @param skuId 商品属性组合（SKU)ID
     * @return
     */
    @Override
    public int getGoodsAttrCountBySkuId(String skuId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("sku.skuId", skuId);
        List<Object> counts = this.goodsAttributeDao.getProperty("COUNT(goodsAttrId)", null, queryParams);
        if (counts != null && !counts.isEmpty()) {
            return Integer.parseInt(String.valueOf(counts.get(0)));
        }
        return 0;
    }

    /**
     * 根据skuId获取此sku的属性组合显示名称 （若 颜色：白色；尺码：M）
     *
     * @param skuId
     * @return
     */
    @Override
    public String getSkuNameBySkuId(String skuId) {

        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("sku.skuId", skuId)
                .sort("attribute.skuAttId", Order.ASC);
        String[] selectors = new String[]{"attribute.skuAttName", "attrValue"};
        List<Object[]> goodAttrNameAndValues = this.goodsAttributeDao.getProperties(selectors, null, queryParams);
        if (goodAttrNameAndValues == null || goodAttrNameAndValues.isEmpty()) {
            return "";
        }
        String temp = "";
        for (Object[] goodAttrNameAndValue : goodAttrNameAndValues) {
            temp += goodAttrNameAndValue[0] + "：" + goodAttrNameAndValue[1] + "；";
        }
        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }
}
