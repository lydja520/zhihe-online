package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.commons.Constant;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/5/9.
 */
public class RealAmountUtilsTest {

    @Test
    public void testRealAmount() throws Exception {
        double _amount = 0.01;
        float _poundage = Constant.ORDER_HANDLER_POUNDAGE_RATE;
        double realAmount = RealAmountUtils.realAmount(_amount, _poundage);
        double poundage = RealAmountUtils.calculatePoundage(_amount, _poundage);
        System.out.println("=============== realAmount:" + realAmount + "==poundage:" + poundage + "=====================");
    }

    @Test
    public void testCalculatePoundage() throws Exception {

    }
}