package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.service.IExamineGoodsService;
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
public class ExamineGoodsServiceImplTest {

    @Resource(name = "examineGoodsService")
    private IExamineGoodsService examineGoodsService;

    @Test
    public void testExecuteSubmitGoodsExamine() throws Exception {
        examineGoodsService.executeSubmitGoodsExamine("3c92b6c5-a106-4b97-ae85-6cce1d2968d0");
        System.out.println("商品已提交审核！");
    }
}