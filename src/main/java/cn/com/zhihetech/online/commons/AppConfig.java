package cn.com.zhihetech.online.commons;

import java.io.Serializable;

/**
 * Created by ShenYunjie on 2016/5/4.
 */
public class AppConfig implements Cloneable, Serializable {

    /**
     * 是否是开发模式
     */
    public final static boolean APP_DEBUG = true;   //是否是开发模式

    /**
     * Appkey
     */
    public final static String APP_KEY = "zhihetech#zhihe_online";

    public class ServerConfig implements Cloneable, Serializable {
        private static final String SERVER_LIVE_DOMAIN = "http://www.zhihetech.com.cn";
        private static final String SERVER_TEST_DOMAIN = "http://localhost:8080";

        public static final String SERVER_DOMAIN = APP_DEBUG ? SERVER_TEST_DOMAIN : SERVER_LIVE_DOMAIN;
    }

    /**
     * 支付宝配置
     */
    public class AliPayConfig implements Cloneable, Serializable {

        private static final String TEST_ALIPAY_DIRECT_PAY_RETRUN_URL = "http://zhihetech.6655.la/alipay/directPayReturnUrl";
        private static final String PRODUCTIONALIPAY_DIRECT_PAY_RETRUN_URL = "http://120.27.24.49/alipay/directPayReturnUrl";
        private static final String TEST_ALIPAY_DIRECT_PAY_NOTIFY_URL = "http://zhihetech.6655.la/alipay/directPayNotifyUrl";
        private static final String PRODUCTIONALIPAY_DIRECT_PAY_NOTIFY_URL = "http://120.27.24.49/alipay/directPayNotifyUrl";

        // 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
        public static final String partner = "2088121199694468";

        // 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
        public static final String seller_id = partner;

        // MD5密钥，安全检验码，由数字和字母组成的32位字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
        public static final String key = "poen4palwufpbqzmll9t3kkz9h3l9jhv";

        // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
        public static final String notify_url = AppConfig.APP_DEBUG ? TEST_ALIPAY_DIRECT_PAY_NOTIFY_URL : PRODUCTIONALIPAY_DIRECT_PAY_NOTIFY_URL;

        // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
        public static final String return_url = AppConfig.APP_DEBUG ? TEST_ALIPAY_DIRECT_PAY_RETRUN_URL : PRODUCTIONALIPAY_DIRECT_PAY_RETRUN_URL;

        // 签名方式
        public static final String sign_type = "MD5";

        // 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
        public static final String log_path = "\\home\\zhihe\\alipay_logs";

        // 字符编码格式 目前支持 gbk 或 utf-8
        public static final String input_charset = "utf-8";

        // 支付类型 ，无需修改
        public static final String payment_type = "1";

        // 调用的接口名，无需修改
        public static final String service = "create_direct_pay_by_user";

        //↓↓↓↓↓↓↓↓↓↓ 请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

        // 防钓鱼时间戳  若要使用请调用类文件submit中的query_timestamp函数
        public static final String anti_phishing_key = "";

        // 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
        public static final String exter_invoke_ip = "";

        //↑↑↑↑↑↑↑↑↑↑请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    }

    /**
     * 极光推送配置
     */
    public class JPushConfig implements Cloneable, Serializable {

        /*mastreSecret*/
        public final static String MASTER_SECRET = "1a4773b8898f74f65fbcf87c";

        /*appKey*/
        public final static String APP_KEY = "16a3f46a7f0e2ae575c2bce2";

        /* 最大重试次数*/
        public final static int MAX_RETRY_TIMES = 5;
    }

    /**
     * 七牛云存储相关配置
     */
    public class QiNiuConfig implements Serializable, Cloneable {

        public final static String AK = "tEGiLzeiKETmXu3X3UZ3MXe9CIRV_KO-qfBD7q8D";
        public final static String SK = "F6FDyNmephHKaZ_Gqin6WK5-lmvw8LrE1HiOXgAs";

        public final static String TEST_BUCKET = "zhihe-test"; //开发环境环境
        public final static String PRODUCTION_BUCKET = "zhihe-online"; //正式环境

        public final static String TEST_DOMIN = "http://o6n0q92ih.bkt.clouddn.com/"; //开发环境环境
        public final static String PRODUCTION_DOMIN = "http://resource.zhihetech.com.cn/"; //正式环境

        public final static String TEST_QINIU_CALLBACK_URL = "http://zhihetech.6655.la/qiniu/api/image/callback";
        public final static String PRODUCTION_QINIU_CALLBACK_URL = "http://120.27.24.49/qiniu/api/image/callback";

        /*存储空间*/
        public final static String BUCKET = AppConfig.APP_DEBUG ? TEST_BUCKET : PRODUCTION_BUCKET;

        /*下载域名*/
        public final static String DOMIN = AppConfig.APP_DEBUG ? TEST_DOMIN : PRODUCTION_DOMIN;

        /*七牛上传图片成功回调地址*/
        public final static String QINIU_CALLBACK_URL = AppConfig.APP_DEBUG ? TEST_QINIU_CALLBACK_URL : PRODUCTION_QINIU_CALLBACK_URL;

        /*七牛上传图片回调传递参数*/
        public final static String QINIU_CALLBACK_BODY =
                "bucket=$(bucket)&key=$(key)&hash=$(etag)&width=$(imageInfo.width)&height=$(imageInfo.height)&owner=$(x:owner)";
    }

    /**
     * 商品订单支付方式
     */
    public class PayType implements Serializable, Cloneable {
        public final static String WX_PAY = "wx";   //微信支付
        public final static String ALI_PAY = "alipay";  //支付宝支付
    }

    /**
     * ping++配置
     */
    public class PingppConfig implements Serializable, Cloneable {
        public static final String PINGPP_API_ID = "app_iTanzP08KGKSCKaT";
        public static final String PINGPP_TEST_API_KEY = "sk_test_CSCCeL98eLO8iDOGe5PqTC8S";//测试环境
        public static final String PINGPP_LIVE_API_KEY = "sk_live_8W580SybDirLevHSa1erTi10";//正式环境

        public static final String PINGPP_API_KEY = AppConfig.APP_DEBUG ? PINGPP_TEST_API_KEY : PINGPP_LIVE_API_KEY;
    }

    /**
     * 环信配置
     */
    public class EasemobConfig implements Serializable, Cloneable {
        // API_HTTP_SCHEMA
        public static final String API_HTTP_SCHEMA = "https";
        // API_SERVER_HOST
        public static final String API_SERVER_HOST = "a1.easemob.com";
        // EM_APP_KEY
        public static final String EM_APP_KEY = "zhihetechvip#zhiheonlie";
        // APP_CLIENT_ID
        public static final String APP_CLIENT_ID = "YXA6FJ_x8PuaEeWXMW809UcYcQ";
        // APP_CLIENT_SECRET
        public static final String APP_CLIENT_SECRET = "YXA6DiU5okkoNkhAKLsB67O4eqgrhQY";
        // DEFAULT_PASSWORD
    }
}
