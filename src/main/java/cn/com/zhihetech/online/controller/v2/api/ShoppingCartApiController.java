package cn.com.zhihetech.online.controller.v2.api;

import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.ParamException;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IShoppingCartService;
import cn.com.zhihetech.online.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by ShenYunjie on 2016/7/13.
 */
@Controller("v2ShoppingCartApiController")
public class ShoppingCartApiController extends V2ApiController {

    @Resource(name = "shoppingCartService")
    private IShoppingCartService shoppingCartService;

    /**
     * 用户添加商品到购物车
     * url:api/v2/user/{userId}/shoppingCart/add    userId为用户ID,不能为空且为有效的用户ID
     * <ul>
     * <li>
     * <h3>参数列表</h3>
     * </li>
     * <li>goods.goodsId    商品ID</li>
     * <li>amount   添加到购物车的数量</li>
     * <li>sku.skuId    对应选择的添加到购物车的商品SKU ID</li>
     * </ul>
     *
     * @param userId
     * @param shoppingCart
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}/shoppingCart/add")
    public ResponseMessage addShoppingCart(@PathVariable(value = "userId") String userId, ShoppingCart shoppingCart) {
        if (StringUtils.isEmpty(userId)) {
            throw new SystemException("请传递用户信息");
        }
        if (shoppingCart.getAmount() <= 0) {
            throw new ParamException("添加数量不能小于或等于0");
        }
        shoppingCart.setUser(new User(userId));
        this.shoppingCartService.add(shoppingCart);
        return executeResult();
    }
}
