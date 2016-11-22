package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.bean.LuckyDraw;
import cn.com.zhihetech.online.bean.LuckyDrawDetail;
import cn.com.zhihetech.online.vo.LuckyDrawAlcObj;
import cn.com.zhihetech.util.hibernate.IQueryParams;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ydc on 16-4-21.
 */
public class LuckyDrawUtils {

    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数
     */
    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int[] _result = null;
        if (n == (max - min + 1)) {
            for (int i = min; i <= max; i++) {
                result[i - 1] = i;
            }
            List list = new ArrayList<>();
            for (int i = 0; i < result.length; i++) {
                list.add(result[i]);
            }
            Collections.shuffle(list);
            _result = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                _result[i] = (int) list.get(i);
            }
            return _result;
        }
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }
}
