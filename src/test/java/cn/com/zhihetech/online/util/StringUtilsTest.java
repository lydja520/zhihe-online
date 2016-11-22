package cn.com.zhihetech.online.util;

import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/3/21.
 */
public class StringUtilsTest extends TestCase {

    @Test
    public void testIsMobileNum() throws Exception {
        boolean result = StringUtils.isMobileNum("16687317685");
        System.out.print(result);
    }

    @Test
    public void testHashSet() {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            String str = "aaaaaa";
            set.add(str);
        }
        System.out.print(set);
    }

    @Test
    public void testStr() {
        String tmp = "需要打开地址进行下一步退款操作: https://mapi.alipay.com/gateway.do?service=refund_fastpay_by_platform_pwd&_input_charset=utf-8&partner=2088121199694468&notify_url=https%3A%2F%2Fapi.pingxx.com%2Fnotify%2Fcharges%2Fch_ev5ivLzrTKqTPuTyX9Sqrzv9%2Frefunds%2Fre_mnLGK8fDaL0Czfrb1KSqDK0S&seller_user_id=2088121199694468&refund_date=2016-05-07%2018%3A15%3A25&batch_no=20160507mnLGK8fDaL0Czfrb1KSqDK0S&batch_num=1&detail_data=2016050721001004440209681749%5E0.01%5E%E7%94%B3%E8%AF%B7%E8%AE%A2%E5%8D%95%E9%80%80%E6%AC%BE&sign=fyhXHD%2BInqHBLbIdHHUdSKktqxGn8IzU2pBneuiZ%2BTnPjJSorZKDoAfXVAWqV79PlbKE5yxbiQyPFQzFsD65xr2638aHpYheKu%2F1a%2FSPIXyeb3ii2PUIPeAUALdHl%2BaxOWgic8YH1HXOKIyCcnp6DqRXAtoCH578K8BOms4Ag4s%3D&sign_type=RSA";
        int i = tmp.indexOf("http");
        String url = tmp.substring(i, tmp.length());
        System.out.println(url);
    }

    @Test
    public void testNum2String() {
        BigDecimal decimal = new BigDecimal(0.0485).setScale(2, BigDecimal.ROUND_HALF_UP);
        //decimal.setScale(2,BigDecimal.ROUND_HALF_UP);
        float value = decimal.floatValue();
        System.out.println(String.valueOf(value));
    }


    public void plzh(String s, List<Integer> iL, int m) {
        char[] is = new char[]{'1', '2', '4', '5', '6', '7', '8', '9'};
        int total = is.length;
        //List<Integer> iL = new ArrayList<Integer>();
        System.out.println("total : " + total);
        if (m == 0) {
            System.out.println(s);
            total++;
            return;
        }
        List<Integer> iL2;
        for (int i = 0; i < is.length; i++) {
            iL2 = new ArrayList<Integer>();
            iL2.addAll(iL);
            if (!iL.contains(i)) {
                String str = s + is[i];
                iL2.add(i);
                plzh(str, iL2, m - 1);
            }
        }
    }

}