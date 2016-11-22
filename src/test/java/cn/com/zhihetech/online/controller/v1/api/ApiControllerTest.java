package cn.com.zhihetech.online.controller.v1.api;

import junit.framework.TestCase;
import org.junit.Test;
import sun.misc.BASE64Decoder;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2016/3/16.
 */
public class ApiControllerTest extends TestCase {

    @Test
    public void testGetToken() throws Exception {
        String userId = "078ef163-33fc-4b9f-ab05-1d7948ba77f5";
        String token = new ApiController().getToken(userId);
        System.out.print(token);
        byte[] aa = new BASE64Decoder().decodeBuffer(token);
        String info = new String(aa);
        System.out.print(info);
    }
}