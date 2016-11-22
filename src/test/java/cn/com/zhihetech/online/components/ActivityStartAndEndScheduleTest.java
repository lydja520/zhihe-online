package cn.com.zhihetech.online.components;

import cn.com.zhihetech.online.service.IActivityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by ydc on 16-7-1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ActivityStartAndEndScheduleTest {

    @Resource(name = "activityService")
    private IActivityService activityService;

    @Test
    public void execute() throws Exception {
        this.activityService.executeActivityStarted("b5737013-7795-4bc1-b7cc-47deb1c5fed6");
    }

}