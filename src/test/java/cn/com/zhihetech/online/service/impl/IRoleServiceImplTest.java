package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Role;
import cn.com.zhihetech.online.service.IMenuService;
import cn.com.zhihetech.online.service.IRoleService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2015/9/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class IRoleServiceImplTest extends TestCase {

    @Resource(name = "roleService")
    private IRoleService roleService;
    @Resource(name = "menuService")
    private IMenuService menuService;
    public void testGetById() throws Exception {

    }

    public void testDelete() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {
        for (int i=0;i<9;i++) {
            Role role = new Role();
            role.setRoleName("test");
            role.setRoleDesc("asdfsadf");

            this.roleService.add(role);
        }

    }

    public void testUpdate() throws Exception {

    }

    public void testGetAllByParams() throws Exception {

    }

    public void testGetPageData() throws Exception {

    }
}