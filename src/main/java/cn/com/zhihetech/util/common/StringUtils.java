package cn.com.zhihetech.util.common;

/**
 * Created by ShenYunjie on 2015/11/12.
 */
public class StringUtils {
    /**
     * 判断字符串是否为null或空格
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {

        return str == null ? true : str.trim().equals("") ? true : false;
    }

    /**
     * 将对象转换为字符串，如果为Null则转换为空字符串
     * @param obj
     * @return
     */
    public static String ObjectToString(Object obj){
        if (obj == null){
            return "";
        }
        return obj.toString();
    }

    public static void test(){
            System.out.println("hello world!");
    }

}
