package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.service.IGoodsTransientInfoService;
import cn.com.zhihetech.online.vo.GoodsTransientInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by ShenYunjie on 2016/7/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class GoodsTransientInfoServiceImplTest {

    @Resource(name = "goodsTransientInfoService")
    private IGoodsTransientInfoService goodsTransientInfoService;

    @Test
    public void testGetGoodsTransientInfoByGoodsId() throws Exception {
        GoodsTransientInfo transientInfo = this.goodsTransientInfoService.getGoodsTransientInfoByGoodsId("3c92b6c5-a106-4b97-ae85-6cce1d2968d0");
        System.out.print(transientInfo);
    }
}