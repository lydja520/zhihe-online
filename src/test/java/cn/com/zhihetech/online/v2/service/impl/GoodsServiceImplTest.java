package cn.com.zhihetech.online.v2.service.impl;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.v2.service.IGoodsService;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by ShenYunjie on 2016/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class GoodsServiceImplTest {
    @Resource(name = "v2GoodsService")
    private IGoodsService goodsService;

    @Test
    public void testGetPageDataByParams() throws Exception {
        PageData<Goods> pageData = this.goodsService.getPageDataByParams(new Pager(1, 20), null);
        System.out.println(pageData);
    }
}