package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.Sku;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IGoodsDao;
import cn.com.zhihetech.online.dao.IShoppingCartDao;
import cn.com.zhihetech.online.dao.ISkuDao;
import cn.com.zhihetech.online.service.IUtilService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/7/14.
 */
@Service("utilService")
public class UtilServiceImpl implements IUtilService {

    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;
    @Resource(name = "skuDao")
    private ISkuDao skuDao;
    @Resource(name = "shoppingCartDao")
    private IShoppingCartDao shoppingCartDao;

    @Override
    public void initInfo() {
        initSku();
        initGoodsStockAndVolumeInfo();
        initShoppingCartInfo();
    }

    /**
     * 将没有sku的商品添加一个默认的sku属性
     */
    @Override
    public void initSku() {
        List<Object> skuGoodsIds = this.skuDao.getProperty("distinct(goodsId)", null, null);
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("onsale", true)
                .andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK);
        if (skuGoodsIds != null && !skuGoodsIds.isEmpty()) {
            queryParams.andNotIn("goodsId", skuGoodsIds);
        }
        List<Object> goodsIds = this.goodsDao.getProperty("goodsId", null, queryParams);
        for (Object obj : goodsIds) {
            String goodsId = obj.toString();
            Goods goods = this.goodsDao.findEntityById(goodsId);
            Sku sku = new Sku();
            sku.setMixCode(goodsId);
            sku.setGoodsId(goodsId);
            sku.setStock(goods.getStock());
            sku.setVolume(goods.getVolume());
            sku.setPrice((float) goods.getPrice());
            this.skuDao.saveEntity(sku);
        }
    }

    /**
     * 将商品的价格、销量、最小价格、最新价格等信息跟新为对应的sku信息
     */
    @Override
    public void initGoodsStockAndVolumeInfo() {
        List<Sku> skus = this.skuDao.getEntities(null);
        IQueryParams queryParams;
        for (Sku sku : skus) {
            queryParams = new GeneralQueryParams()
                    .andEqual("goodsId", sku.getGoodsId());
            Map<String, Object> values = new HashMap<>();
            values.put("price", sku.getPrice());
            values.put("stock", (long) sku.getCurrentStock());
            values.put("volume", sku.getVolume());
            values.put("minPrice", sku.getPrice());
            values.put("maxPrice", sku.getPrice());
            this.goodsDao.executeUpdate(values, queryParams);
        }
    }

    /**
     * 初始化购物车数据对应的sku
     */
    @Override
    public void initShoppingCartInfo() {
        List<Object> ids = this.shoppingCartDao.getProperty("goods.goodsId", null, null);
        for (Object tmp : ids) {
            String goodsId = tmp.toString();
            IQueryParams queryParams = new GeneralQueryParams().andEqual("goodsId", goodsId);
            List<Object> skuIds = this.skuDao.getProperty("skuId", null, queryParams);
            if (skuIds == null || skuIds.isEmpty()) {
                continue;
            }
            queryParams = new GeneralQueryParams().andEqual("goods.goodsId", goodsId);
            Map<String, Object> values = new HashMap<>();
            values.put("sku", new Sku(skuIds.get(0).toString()));
            this.shoppingCartDao.executeUpdate(values, queryParams);
        }
    }
}
