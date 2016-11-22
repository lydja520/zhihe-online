package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.service.IAreaService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class AreaServiceImplTest extends TestCase {

    @Resource(name="areaService")
    private IAreaService areaService;

    @Test
    public void testAdd() throws Exception {
        for(int i =0 ; i <5; i++) {
            Area area = new Area();
            area.setAreaName("yunnan");
            area.setIsRoot(true);
            this.areaService.add(area);
        }
    }

    @Test
    public void testGetAllByParams() throws Exception {
        List<Area> areas = this.areaService.getAllByParams(null,null);
        for(Area area : areas){
            System.out.println(area.getFullName());
        }
    }
}