package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.service.IMerchantService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/4/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MerchantControllerTest {

    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    @Test
    public void testAddIMMerchants() throws Exception {
        this.merchantService.addIMMerchant();
    }
}