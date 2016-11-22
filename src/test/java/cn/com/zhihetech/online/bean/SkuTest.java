package cn.com.zhihetech.online.bean;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by ShenYunjie on 2016/7/6.
 */
public class SkuTest {

    @Test
    public void testGetGoodsAttrMixCode() throws Exception {
        String goodsId = UUID.randomUUID().toString();
        List<GoodsAttribute> goodsAttributes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Goods goods = new Goods();
            goods.setGoodsId(goodsId);
            GoodsAttribute ga = new GoodsAttribute();
            ga.setAttrValue(i+"value");
            SkuAttribute attr = new SkuAttribute();
            attr.setSkuAttId(UUID.randomUUID().toString());
            ga.setAttribute(attr);
            ga.setGoods(goods);
            goodsAttributes.add(ga);
        }
        String tmp = Sku.getGoodsAttrMixCode(goodsId,goodsAttributes);
        System.out.println(tmp.length());
    }
}