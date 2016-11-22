package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.service.IGoodsAttributeSetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2015/12/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class GoodsAttributeSetServiceImplTest {

    @Resource(name = "goodsAttributeSetService")
    public IGoodsAttributeSetService goodsAttributeSetService;

    @Test
    public void testGetById() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {
        GoodsAttributeSet goodsAttributeSet = new GoodsAttributeSet();
        goodsAttributeSet.setGoodsAttSetName("服装");
        goodsAttributeSetService.add(goodsAttributeSet);
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testGetAllByParams() throws Exception {

    }

    @Test
    public void testGetPageData() throws Exception {

    }
}