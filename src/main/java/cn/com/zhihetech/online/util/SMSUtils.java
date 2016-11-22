package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;
import cn.com.zhihetech.online.commons.WebChineseConfig;
import cn.com.zhihetech.online.exception.SystemException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.taglibs.standard.lang.jstl.test.beans.Factory;

import java.io.IOException;

/**
 * 发送短信工具类
 * Created by ShenYunjie on 2016/3/21.
 */
public class SMSUtils extends SerializableAndCloneable {

    private final static Log log = LogFactory.getLog(SMSUtils.class);

    private final static String SMS_URL = "http://utf8.sms.webchinese.cn/";

    /**
     * 异步发送短信
     *
     * @param mobileNum 手机号码
     * @param smsTxt    短信内容
     */
    public static void asyncSendSMS(String mobileNum, String smsTxt) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendSMS(mobileNum, smsTxt);
                }
            }).start();
        } catch (Exception e) {
            log.error("发送短信失败", e);
        }
    }

    /**
     * 向指定的手机号码发送短信
     *
     * @param mobileNum 手机号码
     * @param smsText   短信内容
     * @return
     */
    public static boolean sendSMS(String mobileNum, String smsText) {
        String[] mobileNums = mobileNum.split(",");
        for (int i = 0; i < mobileNums.length; i++) {
            String _mobileNum = mobileNums[i];
            if (!StringUtils.isMobileNum(_mobileNum)) {
                throw new SystemException(_mobileNum + "不是正确的手机号码！");
            }
        }
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(SMS_URL);
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf8");
        NameValuePair[] data = {new NameValuePair("Uid", WebChineseConfig.UID), new NameValuePair("Key", WebChineseConfig.KEY),
                new NameValuePair("smsMob", mobileNum), new NameValuePair("smsText", smsText)};
        post.setRequestBody(data);
        int result = 0;
        try {
            client.executeMethod(post);
            String tmp = new String(post.getResponseBodyAsString().getBytes("utf8"));
            result = Integer.parseInt(tmp);
        } catch (IOException e) {
            new SystemException(e.getMessage());
        }
        switch (result) {
            case -1:
                log.error("发送短信失败，没有该用户账号");
                throw new RuntimeException("没有该用户账号");
            case -2:
                log.error("发送短信失败，接口密钥不正确 [查看密钥],不是账户登陆密码");
                throw new RuntimeException("接口密钥不正确 [查看密钥],不是账户登陆密码");
            case -3:
                log.error("发送短信失败，短信数量不足");
                throw new RuntimeException("短信数量不足");
            case -4:
                log.error("发送短信失败，手机号格式不正确");
                throw new RuntimeException("手机号格式不正确");
            case -6:
                log.error("发送短信失败，IP限制");
                throw new RuntimeException("IP限制");
            case -11:
                log.error("发送短信失败，该用户被禁用");
                throw new RuntimeException("该用户被禁用");
            case -14:
                log.error("发送短信失败，短信内容出现非法字符");
                throw new RuntimeException("短信内容出现非法字符");
            case -21:
                log.error("发送短信失败，MD5接口密钥加密不正确");
                throw new RuntimeException("MD5接口密钥加密不正确");
            case -41:
                log.error("发送短信失败，手机号码为空");
                throw new RuntimeException("手机号码为空");
            case -42:
                log.error("发送短信失败，短信内容为空");
                throw new RuntimeException("短信内容为空");
            case -51:
                log.error("发送短信失败，短信签名格式不正确");
                throw new RuntimeException("短信签名格式不正确");
        }
        return true;
    }
}
