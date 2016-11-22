package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.dao.IGoodsAttributeSetDao;
import cn.com.zhihetech.online.dao.IGoodsDao;
import cn.com.zhihetech.online.dao.ISkuAttributeDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IGoodsAttributeSetService;
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
 * Created by YangDaiChun on 2015/12/4.
 */
@Service("goodsAttributeSetService")
public class GoodsAttributeSetServiceImpl implements IGoodsAttributeSetService {

    @Resource(name = "goodsAttributeSetDao")
    protected IGoodsAttributeSetDao goodsAttributeSetDao;
    @Resource(name = "goodsDao")
    protected IGoodsDao goodsDao;
    @Resource(name = "skuAttributeDao")
    protected ISkuAttributeDao skuAttributeDao;


    @Override
    public GoodsAttributeSet getById(String id) {
        return this.goodsAttributeSetDao.findEntityById(id);
    }

    @Override
    public void delete(GoodsAttributeSet goodsAttributeSet) {
        this.goodsAttributeSetDao.deleteEntity(goodsAttributeSet);
    }

    @Override
    public GoodsAttributeSet add(GoodsAttributeSet goodsAttributeSet) {
        return this.goodsAttributeSetDao.saveEntity(goodsAttributeSet);
    }

    @Override
    public void update(GoodsAttributeSet goodsAttributeSet) {
        this.goodsAttributeSetDao.updateEntity(goodsAttributeSet);
    }

    @Override
    public List<GoodsAttributeSet> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.goodsAttributeSetDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<GoodsAttributeSet> getPageData(Pager pager, IQueryParams queryParams) {
        return this.goodsAttributeSetDao.getPageData(pager, queryParams);
    }

    @Override
    public void updatePermit(String attSetId, boolean permit) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("goodsAttSetId", attSetId);
        Map<String, Object> values = new HashMap<>();
        values.put("permit", permit);
        this.goodsAttributeSetDao.executeUpdate(values, queryParams);
    }

    /**
     * 获取指定商品的属性总数
     *
     * @param goodsId
     * @return
     */
    @Override
    public int getSkuAttrCountByGoodsId(String goodsId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("goodsId", goodsId);
        List<Object> tmp = this.goodsDao.getProperty("goodsAttributeSet.goodsAttSetId", null, queryParams);
        if (tmp == null || tmp.isEmpty()) {
            throw new SystemException("此商品对应的商品类别为空，请联系系统管理员！");
        }
        queryParams = new GeneralQueryParams()
                .andEqual("goodsAttributeSet.goodsAttSetId", tmp.get(0));
        tmp = this.skuAttributeDao.getProperty("COUNT(skuAttId)", null, queryParams);
        if (tmp != null && !tmp.isEmpty()) {
            return Integer.parseInt(String.valueOf(tmp.get(0)));
        }
        return 0;
    }
}
