package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.service.IUtilService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by ShenYunjie on 2016/7/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UtilServiceImplTest {

    @Resource(name = "utilService")
    private IUtilService utilService;

    @Test
    public void testInitInfo() throws Exception {
        utilService.initInfo();
    }

    @Test
    public void testInitSku() throws Exception {
        utilService.initSku();
    }

    @Test
    public void testInitGoodsStockAndVolumeInfo() throws Exception {
        utilService.initGoodsStockAndVolumeInfo();
    }

    @Test
    public void testInitShoppingCartInfo() throws Exception {
        utilService.initShoppingCartInfo();
    }
}