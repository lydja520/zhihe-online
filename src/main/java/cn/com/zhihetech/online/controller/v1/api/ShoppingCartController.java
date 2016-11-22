package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.ParamException;
import cn.com.zhihetech.online.service.IShoppingCartService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ShenYunjie on 2016/1/12.
 */
@Controller
public class ShoppingCartController extends ApiController {
    @Resource(name = "shoppingCartService")
    private IShoppingCartService shoppingCartService;

    /**
     * <h3>将商品添加到购物车</h3>
     * <p>
     * url:api/shoppingCart/add
     * <ul>
     * <li>参数</li>
     * <li>
     * user.userId 用户ID,不能为空
     * </li>
     * <li>
     * goods.goodsId  商品ID,不能为空
     * </li>
     * <li>
     * amount 添加到购物车的商品数量，必须大于0
     * </li>
     * </ul>
     * </p>
     *
     * @param shoppingCart
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "shoppingCart/add", method = RequestMethod.POST)
    public ResponseMessage addShopCart(ShoppingCart shoppingCart) {
        if (shoppingCart.getAmount() <= 0) {
            throw new ParamException("参数错误");
        }
        shoppingCart.setSku(null);
        this.shoppingCartService.add(shoppingCart);
        return executeResult();
    }

    /**
     * <h3>查询用户的购物车</h3>
     * <p>
     * URL:api/user/{userId}/shoppingCarts  其中{userId}为当前用户ID
     * <ul>
     * <li>分页参数</li>
     * </ul>
     * </p>
     *
     * @param request
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/shoppingCarts")
    public PageData<ShoppingCart> getShoppingCartByUserId(HttpServletRequest request, @PathVariable("userId") @NotNull String userId) {
        IQueryParams queryParams = createQueryParams(request);
        return  this.shoppingCartService.getShoppingCartByUserId(userId,this.createPager(request),queryParams);
    }

    /**
     * <h3>根据Id删除购物车数据</h3>
     * <p>
     * url:api/shoppingCart/{shoppingCartId}/delete
     * {shoppingCartId}    购物车数据ID
     * </p>
     *
     * @param shoppingCartId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "shoppingCart/{shoppingCartId}/delete")
    public ResponseMessage deleteShopCart(@PathVariable("shoppingCartId") @NotNull String shoppingCartId) {
        this.shoppingCartService.deleteById(shoppingCartId);
        return executeResult();
    }

    /**
     * <h3>删除多个购物车数据</h3>
     * <p>
     * url: api/shoppingCarts/delete  <br>
     * <p>
     * <h4>参数</h4>
     * shoppingCartsIds  格式  购物车1ID*购物车2ID*购物车3ID*……
     *
     * @param shoppingCartsIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "shoppingCarts/delete")
    public ResponseMessage deleteShopCarts(String shoppingCartsIds) {
        String[] _shoppingCartsIds = shoppingCartsIds.split("\\*");
        this.shoppingCartService.deleteShopCarts(_shoppingCartsIds);
        return executeResult();
    }

    /**
     * <h3>更新购物车数据</h3>
     * url: api/shoppingCart/update  <br>
     * <p>
     * <h4>参数</h4>
     * shoppingCartId：购物车ID
     * amount：商品数量
     *
     * @param shoppingCartId
     * @param amount
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "shoppingCart/update", method = RequestMethod.POST)
    public ResponseMessage updateShopCart(String shoppingCartId, int amount) {
        this.shoppingCartService.updateById(shoppingCartId, amount);
        return executeResult();
    }
}
