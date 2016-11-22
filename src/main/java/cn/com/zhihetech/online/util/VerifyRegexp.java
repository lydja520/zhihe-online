package cn.com.zhihetech.online.util;

import java.util.regex.Pattern;

/**
 * Created by YangDaiChun on 2016/5/16.
 */
public class VerifyRegexp {

    public static boolean isEmpty(String target) {
        return target == null ? true : target.trim().equals("");
    }

    public static String object2String(Object target) {
        return target == null ? "" : String.valueOf(target);
    }

    public static boolean isMobileNum(String paramString) {
        return Pattern.compile(PropertiesUtils.getProperties().getProperty("REGEX_MOBILE")).matcher(paramString).matches();
    }

}
