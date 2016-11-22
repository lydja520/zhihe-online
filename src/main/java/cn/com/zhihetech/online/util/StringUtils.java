package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ShenYunjie on 2016/3/21.
 */
public class StringUtils extends SerializableAndCloneable {

    /**
     * 判断一个对象是否为空或为空白字符串
     *
     * @param target
     * @return
     */
    public static boolean isEmpty(Object target) {
        if (target == null) {
            return true;
        }
        if (target instanceof String) {
            return target.toString().trim().equals("") ? true : false;
        }
        return false;
    }

    /**
     * 判断一个字符串是否为手机号码
     *
     * @param target
     * @return
     */
    public static boolean isMobileNum(String target) {
        final String REGEX_MOBILE = "^(1[3,4,5,7,8][0-9])\\d{8}$";
        Pattern pattern = Pattern.compile(REGEX_MOBILE);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }
}
