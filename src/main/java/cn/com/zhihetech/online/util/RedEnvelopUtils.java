package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 红包分配算法
 * Created by ShenYunjie on 2015/12/14.
 */
public class RedEnvelopUtils extends SerializableAndCloneable {
    public final static int YUAN_FEN_UNIT = 100;    //人名币元与分的转换率（1元=100分）
    private final static String FLOAT_FORMAT = "#.00"; //人名币元，精确到分

    /**
     * 将一个红包随机分成多份
     *
     * @param totalMoney 红包总金额
     * @param count      红包个数
     * @return
     */
    public static List<Float> getRedMoneies(float totalMoney, int count) {
        int total = (int) totalMoney * 100;  //将元转换为分
        if (total < 0 || count < 0) {
            throw new RuntimeException("红包金额和红包分数小于零");
        }
        if (total < count) {
            throw new RuntimeException("红包金额不足分配");
        }
        List<Integer> items = new ArrayList<>(count);
        List<Float> redItems = new ArrayList<>();
        if (total == count) {
            for (int i = 0; i < items.size(); i++) {
                redItems.add((float) ((float) 1 / (float) YUAN_FEN_UNIT));
            }
            return redItems;
        }
        for (int i = 0; i < count; i++) {
            int max = total - (count - i - 1);
            if (i == (count - 1)) {
                items.add(total);
                total = 0;
            } else {
                int tmp = random(1, max);
                items.add(tmp);
                total -= tmp;
            }
        }
        Collections.shuffle(items); //随机打散
        for (int i = 0; i < items.size(); i++) {
            redItems.add((float) ((float) items.get(i) / (float) YUAN_FEN_UNIT));
        }
        return redItems;
    }

    /**
     * 生成一个区间的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int random(int min, int max) {
        if (min == max) {
            return min;
        }
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
}
