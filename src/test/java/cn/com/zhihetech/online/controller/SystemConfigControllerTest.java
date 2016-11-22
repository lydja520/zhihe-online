package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.SystemConfig;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.service.ISystemConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by ydc on 16-8-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SystemConfigControllerTest {

    @Resource(name = "systemConfigService")
    private ISystemConfigService systemConfigService;

    @Test
    public void addSystemConfig(){
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfigType(Constant.WEB_CHINESE_CHATROOM_PERSON_COUNT);
        systemConfig.setConfigDesc("app活动人数最小基数设置");
        systemConfig.setConfigName("活动app活动最少人数设置");
        systemConfig.setConfigValue("30");
        this.systemConfigService.add(systemConfig);
    }

}