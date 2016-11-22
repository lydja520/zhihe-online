package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.service.IUserService;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.util.common.MD5Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/8/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserControllerTest {

    @Resource(name = "userService")
    private IUserService userService;

    @Test
    public void addGuestUser() {
        User user = new User();
        user.setUserPhone("guest");
        user.setUserName("游客");
        user.setPwd(MD5Utils.getMD5Msg("123456"));
        user.setPermit(true);
        user.setAge(20);
        user.setOccupation("无");
        Area area = new Area();
        area.setAreaId("6b001ca8-18c5-40be-9385-9fb388877e07");
        user.setArea(area);
        user.setIncome("4000-5000");
        user.setBirthday(DateUtils.String2DateTime("1990-01-01 00:00:00"));
        user.setCreateDate(new Date());
        user.setChatUserName("guest");
        user.setChatPassword("guest");
        this.userService.add(user);

    }

}