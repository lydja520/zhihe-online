package cn.com.zhihetech.online.service.impl;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by ShenYunjie on 2016/7/12.
 */
public class FileTest {
    @Test
    public void testFile() throws IOException {
        String path = "D://aaa.test";
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        file.hashCode();
    }
}
