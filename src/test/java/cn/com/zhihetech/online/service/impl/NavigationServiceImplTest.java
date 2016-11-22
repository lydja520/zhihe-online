package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Navigation;
import cn.com.zhihetech.online.dao.INavigationDao;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by YangDaiChun on 2015/12/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class NavigationServiceImplTest extends TestCase{

    @Resource(name = "navigationDao")
    private INavigationDao navigationDao;

    @Test
    public void add(){
        Navigation navigation = new Navigation();
        navigation.setNavigationName("今日上新");
        navigation.setDesc("今日上新导航");
        navigation.setPermit(true);
        navigation.setOrder(1);
        navigation.setViewTargert("2321");
        this.navigationDao.saveEntity(navigation);
    }
}