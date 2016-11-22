package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.service.IGoodsAttributeService;
import cn.com.zhihetech.online.vo.GoodsAttrInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ShenYunjie on 2016/7/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class GoodsAttributeServiceImplTest {
    @Resource(name = "goodsAttributeService")
    private IGoodsAttributeService goodsAttributeService;
    @Test
    public void testGetGoodsAttrInfosByGoodsId() throws Exception {
        List<GoodsAttrInfo> goodsAttrInfoList = this.goodsAttributeService.getGoodsAttrInfosByGoodsId("3c92b6c5-a106-4b97-ae85-6cce1d2968d0");
        System.out.println(goodsAttrInfoList.size());
    }
}