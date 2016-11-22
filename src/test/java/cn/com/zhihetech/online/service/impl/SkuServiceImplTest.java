package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Sku;
import cn.com.zhihetech.online.service.ISkuService;
import cn.com.zhihetech.online.util.StringUtils;
import cn.com.zhihetech.online.vo.GoodsAttrSkuInfo;
import cn.com.zhihetech.online.vo.GoodsSkuInfo;
import cn.com.zhihetech.util.common.MD5Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ShenYunjie on 2016/7/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SkuServiceImplTest {

    @Resource(name = "skuService")
    private ISkuService skuService;

    @Test
    public void testAddSkuByGoodsId() throws Exception {
        GoodsAttrSkuInfo goodsAttrInfo = this.skuService.getGoodsAttrSkuInfoByGoodsId("3c92b6c5-a106-4b97-ae85-6cce1d2968d0");
        List<GoodsSkuInfo> goodsSkuInfos = goodsAttrInfo.getGoodsSkuInfos();
        List<String> codes = new ArrayList<>();
        for (GoodsSkuInfo skuInfo : goodsSkuInfos) {
            String code = "";
            for (String tmp : skuInfo.getAttrCodes()) {
                code += tmp + ";";
            }
            code = StringUtils.isEmpty(code) ? code : code.substring(0, code.length() - 1);
            code = new String(code.getBytes(), "UTF-8").trim();
            code = MD5Utils.getMD5Msg(code);
            if (code.equals(skuInfo.getMixCode())) {
                System.out.println(code);
            }
            codes.add(code);
        }
        System.out.println(codes);
    }

    public static String getMd5Msg(String msg) {
        return MD5Utils.getMD5Msg(msg);
    }

    @Test
    public void testGetDefaultSkuByGoodsId() throws Exception {
        Sku sku = this.skuService.getDefaultSkuByGoodsId("3c92b6c5-a106-4b97-ae85-6cce1d2968d0");
        System.out.println(sku);
    }
}