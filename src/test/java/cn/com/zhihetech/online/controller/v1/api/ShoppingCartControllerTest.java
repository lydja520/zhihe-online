package cn.com.zhihetech.online.controller.v1.api;

import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.service.IShoppingCartService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ShoppingCartControllerTest {

    @Resource(name = "shoppingCartService")
    private IShoppingCartService shoppingCartService;

    @Test
    public void getShoppingCartByUserId() throws Exception {
        String userId = "21124339-5a96-403d-9821-0879b23771b9";
        PageData<ShoppingCart> shoppingCartPageData = this.shoppingCartService.getShoppingCartByUserId(userId, new Pager(), new GeneralQueryParams());
        System.out.println(shoppingCartPageData);
    }

}