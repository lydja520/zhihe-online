package cn.com.zhihetech.online.util;

import cn.com.zhihetech.util.common.MD5Utils;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/7/8.
 */
public class CombineAlgorithmTest {
    @Test
    public void main() throws Exception {
        int count = 0;
        long start = System.currentTimeMillis();
        String[] a = new String[]{"M", "白色", "短袖","男款"};
        for (int i = 1; i <= a.length; i++) {
            CombineAlgorithm ca = new CombineAlgorithm(a, i);

            Object[][] c = ca.getResutl();
            for (int j = 0; j < c.length; j++) {
                count++;
                String code = "";
                for (int n = 0; n < c[j].length; n++) {
                    code += UUID.randomUUID().toString() + ":" + c[j][n] + ";";
                    //System.out.println(Arrays.toString(obj));
                }
                code = StringUtils.isEmpty(code) ? "" : code.substring(0, code.length() - 1);
                System.out.println(code);
                System.out.println(MD5Utils.getMD5Msg(code));
            }
        }
        System.out.println("一共：" + count + " 种组合");
        System.out.println("共花费"+ (System.currentTimeMillis() - start));
    }

}