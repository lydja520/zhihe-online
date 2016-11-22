package cn.com.zhihetech.online.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * PropertiesUtils
 *
 * @author ydc 2014-09-15
 */
public class PropertiesUtils {

    public static Properties getProperties() {

        Properties properties = new Properties();

        try {
            InputStream inputStream = PropertiesUtils.class
                    .getClassLoader()
                    .getResourceAsStream("projectConfig.properties");
            properties.load(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

}
