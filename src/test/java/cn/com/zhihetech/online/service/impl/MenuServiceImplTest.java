package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Menu;
import cn.com.zhihetech.online.service.IMenuService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MenuServiceImplTest extends TestCase {

    @Resource(name = "menuService")
    private IMenuService menuService;

    public void testGetById() throws Exception {

    }

    public void testDelete() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {
        for (int i = 0; i < 5; i++) {
            Menu menu = new Menu();
            menu.setIsRoot(true);
            menu.setMenuDesc("这是一条测试数据");
            menu.setMenuOrder(0);
            menu.setMenuName("系统管理");
            menu.setMenuUrl("admin/test");
            this.menuService.add(menu);
        }
    }

    public void testUpdate() throws Exception {

    }

    @Test
    public void testGetAllByParams() throws Exception {
        Pager pager = new Pager(2, 20);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andAllLike("menuName", "系统");
        List<Menu> menus = this.menuService.getAllByParams(pager, queryParams);
        for (Menu menu : menus) {
            System.out.println(menu.getMenuName());
        }
        System.out.println(menus.size());
    }

    @Test
    public void testGetPageData() throws Exception {
        Pager pager = new Pager(2, 20);
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andAllLike("menuName", "系统");
        long current = new Date().getTime();
        for (int i = 0; i < 10000; i++) {
            PageData<Menu> menuPageData = this.menuService.getPageData(pager, queryParams);
        /*for(Menu menu : menuPageData.getRows()){
            System.out.println(menu.getMenuName());
        }*/
            System.out.println(menuPageData.getPage());
            System.out.println(menuPageData.getPageSize());
            System.out.println(menuPageData.getTotal());
            System.out.println(menuPageData.getTotalPage());
        }
        System.out.println(new Date().getTime() - current);
    }
}