package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.SkuAttribute;
import cn.com.zhihetech.online.service.ISkuAttributeService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by YangDaiChun on 2015/12/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SkuAttributeServiceImplTest extends TestCase {

    @Resource(name = "skuAttributeService")
    private ISkuAttributeService skuAttributeService;

    @Test
    public void testAdd() throws Exception {
       /* SkuAttribute skuAttribute = new SkuAttribute();
        skuAttribute.setSkuAttCode("002");
        skuAttribute.setSkuAttName("颜色");
        skuAttribute.setParentySkuAttribute(null);
        this.skuAttributeService.add(skuAttribute);*/
    }
}