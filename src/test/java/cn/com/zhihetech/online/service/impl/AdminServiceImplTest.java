package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.util.common.MD5Utils;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class AdminServiceImplTest extends TestCase {

    @Resource(name="adminService")
    private IAdminService adminService;

    @Test
    public void testAdd() throws Exception {
        Admin admin = new Admin();
        admin.setAdminCode("lydja");
        admin.setAdminPwd("123456");
        admin.setPermit(true);
        this.adminService.add(admin);
    }

    @Test
    public void testGetMerchant() throws Exception {

    }

    @Test
    public void testGetMerchantId() throws Exception {

    }
}



