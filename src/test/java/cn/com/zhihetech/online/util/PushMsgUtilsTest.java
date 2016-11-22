package cn.com.zhihetech.online.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ShenYunjie on 2016/5/5.
 */
public class PushMsgUtilsTest {

    @Test
    public void testPushToUserByUserId() throws Exception {
        PushMsgUtils.PushToUserByUserId("bc368c20474e42b996e27a76bb31be58", "服务器端测试同送");
    }
}