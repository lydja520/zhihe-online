package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by ShenYunjie on 2016/1/12.s
 */
public interface IShoppingCartService extends SupportService<ShoppingCart> {
    /**
     * 根据ID删除购物车中的商品
     *
     * @param shoppingCartId
     */
    void deleteById(String shoppingCartId);

    /**
     * 根据购物车ID更新购物车
     *
     *
     * @param shoppingCartId
     * @param amount
     */
    void updateById(String shoppingCartId,int amount);

    void deleteShopCarts(String[] shoppingCartIds);

    /**
     * 根据用户Id获取到该用户购物车里的所有商品
     * @param userId
     * @param pager
     * @param queryParams
     * @return
     */
    PageData<ShoppingCart> getShoppingCartByUserId(String userId, Pager pager, IQueryParams queryParams);
}
