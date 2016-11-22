package cn.com.zhihetech.util.common;

import cn.com.zhihetech.online.exception.ParamException;
import cn.com.zhihetech.online.exception.SystemException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ShenYunjie on 2015/11/18.
 */
public class MD5Utils {
    public static String getMD5Msg(String target) {
        if (StringUtils.isEmpty(target)) {
            throw new ParamException("加密内容不能为空");
        }
        byte[] source = target.getBytes();
        String s = null;
        // 用来将字节转换成16进制表示的字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException("加密密码时出错，用户密码不能包含特殊字符", e);
        }
        md.update(source);
        // MD5的计算结果是一个128位的长整数，用字节表示为16个字节
        byte[] tmp = md.digest();
        // 每个字节用16进制表示的话，使用2个字符(高4位一个,低4位一个)，所以表示成16进制需要32个字符
        char[] str = new char[16 * 2];
        int k = 0;// 转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) {// 对MD5的每一个字节转换成16进制字符
            byte byte0 = tmp[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 对字节高4位进行16进制转换
            str[k++] = hexDigits[byte0 & 0xf]; // 对字节低4位进行16进制转换
        }
        s = new String(str);
        return s;
    }
}
