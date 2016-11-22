package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.GoodsDetail;
import cn.com.zhihetech.online.dao.IGoodsDetailDao;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by YangDaiChun on 2016/1/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class GoodsDetailServiceImplTest extends TestCase{

    @Resource(name ="goodsDetailDao")
    private IGoodsDetailDao goodsDetailDao;

    @Test
    public void testAdd() throws Exception {
        GoodsDetail goodsDetail = new GoodsDetail();
        goodsDetail.setViewType(1);
        goodsDetail.setViewTarget(2+"");
        goodsDetail.setGoods(null);
        goodsDetail.setImgInfo(null);
        this.goodsDetailDao.saveEntity(goodsDetail);
    }
}