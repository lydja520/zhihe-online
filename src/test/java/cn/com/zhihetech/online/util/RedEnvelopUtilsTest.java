package cn.com.zhihetech.online.util;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

/**
 * Created by ShenYunjie on 2015/12/14.
 */
public class RedEnvelopUtilsTest extends TestCase {

    @Test
    public void testGetItemAmouts() throws Exception {
        List<Float> items = RedEnvelopUtils.getRedMoneies(100, 12);
        float total = 0l;
        for (Float item : items) {
            System.out.println(item);
            total += item;
        }
        System.out.println(total);
    }
}