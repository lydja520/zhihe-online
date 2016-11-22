package cn.com.zhihetech.online.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ShenYunjie on 2016/6/18.
 */
public class NumberUtilsTest {

    @Test
    public void testDoubleScale() throws Exception {
        double num = 11.3413412;
        System.out.print(NumberUtils.doubleScale(2, num));
    }

    @Test
    public void testFloatScale() throws Exception {
        double num = 11.3413412;
        System.out.print(NumberUtils.floatScale(2, 11.335314 + 0.01));
    }

    @Test
    public void testNum2StringScale() throws Exception {
        double num = 11.3413412;
        System.out.print(NumberUtils.num2StringScale(2, num));
    }


    @Test
    public void testList() throws Exception {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String tmp = new String("asjf'skdaflsad'fk");
            if (list.contains(tmp)) continue;
            list.add(tmp);
        }
        System.out.print(list);
    }
}