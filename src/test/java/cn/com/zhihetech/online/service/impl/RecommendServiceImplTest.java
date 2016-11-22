package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.service.IRecommendService;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ShenYunjie on 2016/7/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RecommendServiceImplTest {

    @Resource(name = "recommendService")
    private IRecommendService recommendService;

    @Test
    public void testGetRecommendGoodsByMerchantId() throws Exception {
        List<Goods> goodses = this.recommendService.getRecommendGoodsByMerchantId(new Pager(1, 50), "7904e451-3fbb-479e-a9a5-a7cc080a85bd");
        for(Goods goods : goodses){
            if(goods.getStock()>0){
                System.out.println(goods.getDisplayState());
            }
        }
        System.out.println(goodses);
    }
}