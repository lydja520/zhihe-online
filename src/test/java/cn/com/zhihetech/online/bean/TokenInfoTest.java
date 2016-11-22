package cn.com.zhihetech.online.bean;


import org.junit.Test;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ShenYunjie on 2016/4/19.
 */
public class TokenInfoTest {

    @Test
    public void testJson() throws Exception {
        TokenInfo token = new TokenInfo();
        token.setDateStamp(new Date().getTime());
        token.setUserCode("adfasdf");
        token.setUserId("asdfasfsadfsadf");
        token.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        System.out.println(token.encrypt());
        TokenInfo _token = token.decrypt(token.encrypt());
        System.out.print(_token);
    }
}