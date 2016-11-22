package cn.com.zhihetech.online.util;

import java.math.BigDecimal;

/**
 * Created by YangDaiChun on 2016/5/9.
 */
public class RealAmountUtils {

    public static double realAmount(double amount, float poundageRate) {
        double realAmount = 0f;
        double poundage = 0f;
        poundage = amount * poundageRate;
        BigDecimal poundageBg = new BigDecimal(poundage).setScale(2, BigDecimal.ROUND_HALF_UP);
        realAmount = amount - poundageBg.doubleValue();
        BigDecimal realAmountBg = new BigDecimal(realAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
        return realAmountBg.doubleValue();
    }

    public static double calculatePoundage(double amount, float poundageRate) {
        double poundage = 0f;
        poundage = amount * poundageRate;
        BigDecimal poundageBg = new BigDecimal(poundage).setScale(2, BigDecimal.ROUND_HALF_UP);
        return poundageBg.doubleValue();
    }
}
