package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.bean.Sku;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IShoppingCartDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IShoppingCartService;
import cn.com.zhihetech.online.service.ISkuService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/1/12.
 */
@Service("shoppingCartService")
public class ShoppingCartServiceImpl implements IShoppingCartService {

    @Resource(name = "shoppingCartDao")
    private IShoppingCartDao shoppingCartDao;
    @Resource(name = "skuService")
    private ISkuService skuService;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public ShoppingCart getById(String id) {
        return this.shoppingCartDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param shoppingCart 需要删除的持久化对象
     */
    @Override
    public void delete(ShoppingCart shoppingCart) {
        this.deleteById(shoppingCart.getShoppingCartId());
    }

    /**
     * 添加一个对象到数据库
     *
     * @param shoppingCart 需要持久化的对象
     * @return
     */
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        if (shoppingCart.getSku() == null) {
            shoppingCart.setSku(this.skuService.getDefaultSkuByGoodsId(shoppingCart.getGoods().getGoodsId()));
        }
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("user.userId", shoppingCart.getUser().getUserId())
                .andEqual("goods.goodsId", shoppingCart.getGoods().getGoodsId())
                .andEqual("sku.skuId", shoppingCart.getSku().getSkuId());
        List<ShoppingCart> shoppingCarts = this.shoppingCartDao.getEntities(queryParams);
        if (shoppingCarts != null && shoppingCarts.size() > 0) {
            ShoppingCart tmpCart = shoppingCarts.get(0);
            tmpCart.setAmount(tmpCart.getAmount() + shoppingCart.getAmount());
            this.update(tmpCart);
            return tmpCart;
        }
        shoppingCart.setFocusDate(new Date());
        return this.shoppingCartDao.saveEntity(shoppingCart);
    }

    /**
     * 更新一个持久化对象
     *
     * @param shoppingCart
     */
    @Override
    public void update(ShoppingCart shoppingCart) {
        this.shoppingCartDao.updateEntity(shoppingCart);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<ShoppingCart> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.shoppingCartDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<ShoppingCart> getPageData(Pager pager, IQueryParams queryParams) {
        return this.shoppingCartDao.getPageData(pager, queryParams);
    }

    @Override
    public void deleteById(String shoppingCartId) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("shoppingCartId", shoppingCartId);
        this.shoppingCartDao.executeDelete(queryParams);
    }

    @Override
    public void updateById(String shoppingCartId, int amount) {
        Sku sku = findSkuByShoppingCartId(shoppingCartId);
        if (sku != null && amount > sku.getCurrentStock()) {
            throw new SystemException("商品库存不足！");
        }
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("shoppingCartId", shoppingCartId)
                .andEqual("sku.skuId", sku.getSkuId());
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("amount", amount);
        this.shoppingCartDao.executeUpdate(paramAndValue, queryParams);
    }

    @Override
    public void deleteShopCarts(String[] shoppingCartIds) {
        if (shoppingCartIds == null || shoppingCartIds.length < 1) {
            return;
        }
        for (String shoppingCartId : shoppingCartIds) {
            this.shoppingCartDao.deleteEntity(this.shoppingCartDao.findEntityById(shoppingCartId));
        }
    }

    /**
     * 根据用户Id获取到该用户购物车里的所有商品
     *
     * @param userId
     * @param pager
     * @param queryParams
     * @return
     */
    @Override
    public PageData<ShoppingCart> getShoppingCartByUserId(String userId, Pager pager, IQueryParams queryParams) {
        queryParams.andEqual("goods.deleteState", false)
                .andEqual("goods.examinState", Constant.EXAMINE_STATE_EXAMINED_OK)
                .andEqual("goods.onsale", true)
                .andEqual("goods.merchant.permit", true)
                .andProParam("goods.stock > 0")
                .andEqual("goods.merchant.examinState", Constant.EXAMINE_STATE_EXAMINED_OK)
                .andEqual("user.userId", userId);
        PageData<ShoppingCart> shoppingCartPageData = this.shoppingCartDao.getPageData(pager, queryParams);
        List<ShoppingCart> shoppingCarts = shoppingCartPageData.getRows();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            String skuId = shoppingCart.getSku().getSkuId();
            String skuValue = this.skuService.getSkuValueBySkuId(skuId);
            shoppingCart.getSku().setSkuValue(skuValue);
        }
        return shoppingCartPageData;
    }

    /**
     * 根据购物车内ID获取购物车商品对应的SKU(相同商品，对应不同SKU在购物城中为不同的数据）
     */
    protected Sku findSkuByShoppingCartId(String shoppingCartId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("shoppingCartId", shoppingCartId);
        List<Object> skuIds = this.shoppingCartDao.getProperty("sku.skuId", null, queryParams);
        if (skuIds == null || skuIds.isEmpty()) {
            return null;
        }
        return this.skuService.getById(skuIds.get(0).toString());
    }

}
