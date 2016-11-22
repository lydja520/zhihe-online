package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.service.IShoppingCartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/1/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ShoppingCartServiceImplTest {

    @Resource(name = "shoppingCartService")
    private IShoppingCartService shoppingCartService;

    @Test
    public void testAdd() throws Exception {
    }
}