package cn.com.zhihetech.online.util;

import org.junit.Test;
import org.springframework.cglib.core.EmitUtils;

import static org.junit.Assert.*;

/**
 * Created by YangDaiChun on 2016/8/19.
 */
public class EMChatUtilsTest {
    @Test
    public void deleteChatRoom() throws Exception {
        EMChatUtils.deleteChatRoom("231681314288304572");
    }

}