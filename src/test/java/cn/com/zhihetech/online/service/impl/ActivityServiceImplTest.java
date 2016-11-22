package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.service.IActivityService;
import cn.com.zhihetech.online.service.IGoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ShenYunjie on 2016/4/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ActivityServiceImplTest {
    @Test
    public void executeActivityStarted() throws Exception {

    }

    @Test
    public void onActivityStartedInitGoodsSaleVolume() throws Exception {

    }

    @Resource(name = "activityService")
    private IActivityService activityService;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;

    @Test
    public void testUpdateSubmitState() throws Exception {
        //activityService.updateSubmitState("11111111111");
        List<String> ids = new ArrayList<>();
        ids.add("02fb401a-3ba2-4c1c-a2f6-a60e1b1d3ea7");
        ids.add("03b98309-ed59-47a2-837f-33589aaecc22");
        ids.add("22816630-fec1-464c-8a9e-aac2f5c87a57");
        ids.add("391018d3-3905-4b6a-8e42-6ac62f1970a1");
        List<Long> counts = new ArrayList<>();
        counts.add(1l);
        counts.add(1l);
        counts.add(1l);
        counts.add(1l);
        this.goodsService.executeAddVolumeByGoodsIdsAndGoodsCounts(ids, counts);
    }
}