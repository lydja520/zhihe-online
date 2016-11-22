package cn.com.zhihetech.util.common;

import java.util.Random;

/**
 * Created by YangDaiChun on 2016/4/13.
 */
public class GeneratedNRandom {
    public static String generated(int strLength) {

        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }
}
