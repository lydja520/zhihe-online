package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.service.IMerchantService;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by YangDaiChun on 2016/4/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MerchantServiceImplTest extends TestCase {

    @Resource(name = "merchantService")
    private IMerchantService merchantService;

    public void testAddIMMerchant() throws Exception {

    }
}