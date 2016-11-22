package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.bean.District;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.IDistrictService;
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
public class DistrictServiceImplTest extends TestCase {

    @Resource(name="districtService")
    private IDistrictService districtService;

    @Test
    public void testAdd() throws Exception {
        Area area = new Area();
        area.setAreaName("yun");
        for(int i =0 ; i <5; i++) {
            District district = new District();
            district.setDistrictName("yun1");
            districtService.add(district);
        }
    }
}