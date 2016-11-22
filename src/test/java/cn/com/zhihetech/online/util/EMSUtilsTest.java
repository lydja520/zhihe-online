package cn.com.zhihetech.online.util;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Administrator on 2016/3/21.
 */
public class EMSUtilsTest extends TestCase {

    @Test
    public void testSendSMSM() throws Exception {
        String tmplete = "尊敬的用户，您正通过实淘账号提取现金，验证码是123456，有效时间为1分钟。感谢您对实淘的支持。【实淘】";
        boolean result = SMSUtils.sendSMS("18687317688", tmplete);
        System.out.println(result);
    }
}