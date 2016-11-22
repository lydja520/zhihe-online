package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.bean.SkuAttribute;
import cn.com.zhihetech.online.dao.ISkuAttributeDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.ISkuAttributeService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2015/12/2.
 */
@Service("skuAttributeService")
public class SkuAttributeServiceImpl implements ISkuAttributeService {

    @Resource(name = "skuAttributeDao")
    public ISkuAttributeDao skuAttributeDao;

    @Override
    public SkuAttribute getById(String id) {
        return this.skuAttributeDao.findEntityById(id);
    }

    @Override
    public void delete(SkuAttribute skuAttribute) {
        this.skuAttributeDao.deleteEntity(skuAttribute);
    }

    @Override
    public SkuAttribute add(SkuAttribute skuAttribute) {
        skuAttribute.setSkuAttName(skuAttribute.getSkuAttName().trim());
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goodsAttributeSet", skuAttribute.getGoodsAttributeSet())
                .andEqual("skuAttName", skuAttribute.getSkuAttName());
        List<Object> tmps = this.skuAttributeDao.getProperty("skuAttId", null, queryParams);
        if (tmps != null && !tmps.isEmpty()) {
            throw new SystemException("此商品类别的商品属性已存在,请勿重复添加！");
        }
        return this.skuAttributeDao.saveEntity(skuAttribute);
    }

    @Override
    public void update(SkuAttribute skuAttribute) {
        //this.skuAttributeDao.updateEntity(skuAttribute);
    }

    @Override
    public List<SkuAttribute> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.skuAttributeDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<SkuAttribute> getPageData(Pager pager, IQueryParams queryParams) {
        return this.skuAttributeDao.getPageData(pager, queryParams);
    }

    @Override
    public void executeUpdateParamsById(String attrId, String attrName, String goodsAttrSetId, String attrDesc) {
        attrName = attrName.trim();
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goodsAttributeSet.goodsAttSetId", goodsAttrSetId)
                .andEqual("skuAttName", attrName)
                .andNotEq("skuAttId", attrId);
        List<Object> tmps = this.skuAttributeDao.getProperty("skuAttId", null, queryParams);
        if (tmps != null && !tmps.isEmpty()) {
            throw new SystemException("此商品类别的商品属性已存在,请勿重复添加！");
        }
        queryParams = new GeneralQueryParams().andEqual("skuAttId", attrId);
        Map<String, Object> values = new HashMap<>();
        GoodsAttributeSet gas = new GoodsAttributeSet(goodsAttrSetId);
        values.put("goodsAttributeSet", gas);
        values.put("skuAttName", attrName);
        values.put("skuAttDesc", attrDesc);
        this.skuAttributeDao.executeUpdate(values, queryParams);
    }

    @Override
    public void executeUpdatePermit(String skuAttrId, boolean permit) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("skuAttId", skuAttrId);
        Map<String, Object> values = new HashMap<>();
        values.put("permit", permit);
        this.skuAttributeDao.executeUpdate(values, queryParams);
    }

    @Override
    public List<Object[]> getProperties(String[] skuAttInfo, Pager pager, IQueryParams queryParams) {
        return this.skuAttributeDao.getProperties(skuAttInfo, pager, queryParams);
    }

    @Override
    public List<Object> getProperty(String skuAttName, Pager pager, IQueryParams queryParams) {
        return this.skuAttributeDao.getProperty(skuAttName,pager,queryParams);
    }
}
