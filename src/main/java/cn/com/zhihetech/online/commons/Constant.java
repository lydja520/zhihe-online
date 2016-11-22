package cn.com.zhihetech.online.commons;

import cn.com.zhihetech.online.util.PropertiesUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 系统常量
 * Created by ShenYunjie on 2015/11/17.
 */
public class Constant implements Serializable, Cloneable {

    public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";    //默认日期格式
    public final static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";    //默认日期时间格式

    /**
     * 商家结算手续利率
     */
    public static final float MERCHANT_BILL_POUNDAGE_RATE =
            new BigDecimal(Float.parseFloat(PropertiesUtils.getProperties().getProperty("MERCHANT_BILL_POUNDAGE_RATE")))
                    .setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
    /**
     * 订单结算手续利率
     */
    public static final float ORDER_HANDLER_POUNDAGE_RATE =
            new BigDecimal(Float.parseFloat(PropertiesUtils.getProperties().getProperty("ORDER_HANDLER_POUNDAGE_RATE")))
                    .setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();

    /**
     * 商家参与活动收取的活动经费
     */
    public static final float MERCHNT_ACTIVITY_BUDGET =
            new BigDecimal(Float.parseFloat(PropertiesUtils.getProperties().getProperty("MERCHNT_ACTIVITY_BUDGET")))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

    /**
     * 商家自己推荐商品数量最大值
     */
    public final static int MERCHANT_RECOMMEND_MAX = 20;

    /**
     * 默认密码
     */
    public final static String DEFAULT_PASSWORD = "123456";

    /**
     * 数据分页相关，默认从第一页
     */
    public final static int DEFAULT_PAGE = 1;

    /**
     * 数据分页相关，默认每页数据显示条数（默认每页显示20条数据）
     */
    public final static int DEFAULT_ROWS = 20;

    /**
     * 商户、商品审核状态
     */
    public final static int EXAMINE_STATE_NOT_SUBMIT = 1;   //未提交审核
    public final static int EXAMINE_STATE_SUBMITED = 2;   //待审核
    public final static int EXAMINE_STATE_EXAMINED_OK = 3;   //已审核通过
    public final static int EXAMINE_STATE_EXAMINED_NUOK = 4;   //已审核未通过

    /**
     * 活动审核状态
     */
    public final static int ACTIVITY_STATE_UNSBUMIT = 1; //未提交申请
    public final static int ACTIVITY_STATE_SBUMITED = 2; //已提交申请未审核
    public final static int ACTIVITY_STATE_EXAMINED_OK = 3; //已审核通过
    public final static int ACTIVITY_STATE_EXAMINED_NOT = 4; //已审核未通过
    public final static int ACTIVITY_STATE_STARTED = 5; //活动已开始
    public final static int ACTIVITY_STATE_FNISHED = 6; //活动已结束

    /**
     * 优惠券类型
     */
    public final static int COUPON_DISCOUNT_TYPE = 1;   //打折卷
    public final static int COUPON_VOUCHER_TYPE = 2;    //代金券

    /**
     * App端viewType相关
     */
    public final static int BANNER_VIEWTYPE_TARGET = 1; //跳转到viewTarget指定模块
    public final static int BANNER_VIEWTYPE_MERCHANT = 2;//跳转到商户页面
    public final static int BANNER_VIEWTYPE_GOODS = 3;//跳转到商品页面
    public final static int BANNER_VIEWTYPE_ACTIVITY = 4;//跳转到活动页面
    public final static int BANNER_VIEWTYPE_PAGE = 5;//跳转到指定页面

    /**
     * APP端viewTarget相关
     */
    public final static String NAV_ONE = "1";//投诉维权
    public final static String NAV_TWO = "2";//购物中心
    public final static String NAV_THREE = "3";//新闻发布会
    public final static String NAV_FOUR = "4";//特色街区
    public final static String NAV_FIVE = "5";//优“+”店
    public final static String NAV_SIX = "6";//分类

    /**
     * 验证码类型
     */
    public final static int SECURITY_REGISTER = 1;   //注册验证码
    public final static int SECURITY_ALTERPWD = 2; //修改密码验证码
    public final static int SECURITY_WITHDRAW_MONEY = 3; //提现时获取验证码
    public final static int COMMON_SMS_MESSAGE = 4; //普通短信

    /**
     * 验证码有效期
     */
    public final static int VALIDITY_ONE_MINUTE = 60 * 1000;

    /**
     * 搜索类型相关
     */
    public final static int SEARCH_GOODS = 1;//商品
    public final static int SEARCH_MERCHANT = 2;//商家
    public final static String ENCODING = "UTF-8";

    /**
     * 轮播图所处的位置
     */
    public final static int BANNER_ONE = 1;  //主页面上的轮播图
    public final static int BANNER_TWO = 2;  //优“+”店上的轮播图
    public final static int BANNER_THREE = 3;  //大品牌上的轮播图
    public final static int BANNER_FOUR = 4; //买昆明上的轮播图

    /**
     * 订单状态
     */
    public static final int ORDER_STATE_NO_SUBMIT = 1;  //订单未提交
    public static final int ORDER_STATE_NO_PAYMENT = 2;   //订单已提交，待支付
    public static final int ORDER_STATE_NO_DISPATCHER = 3;  //订单已经支付，等待发货
    public static final int ORDER_STATE_ALREADY_DISPATCHER = 4;  //订单已发货，等待确认收货
    public static final int ORDER_STATE_ALREADY_DELIVER = 5;   //订单已经确认收货
    public static final int ORDER_STATE_ALREADY_CANCEL = 6;  //订单已取消
    public static final int ORDER_STATE_WAIT_REFUND = 7;  //订单等待退款
    public static final int ORDER_STATE_ALREADY_REFUND = 8; //订单退款成功
    public static final int ORDER_STATE_ALREADY_EVALUATE = 9; //评论成功
    public static final int ORDER_STATE_REFUNDING = 10;  //退款中
    public static final int ORDER_STATE_ALREADY_GENERATE_BILL = 11; //该订单已经生成结算单，未结算
    public static final int ORDER_STATE_ALREADY_BILL = 12;  //该订单已经结算
    public static final int ORDER_STATE_PAYDING = 20;   //用户已支付成成功，等待第三方支付回调（为防止用户重复支付）

    /**
     * 订单查询时复合状态
     */
    public static final int ORDER_STATE_REFUND = 101;  //获取待退款和已退款成功的订单
    public static final int ORDER_STATE_FINISH = 102; //获取已签收和已评价的订单

    /**
     * 买家申请体现状态
     */
    public static final int APPLY_WITHDRAW = 1;  //申请提现
    public static final int WITHDRAW_OK = 2;  //提现成功
    public static final int WITHDRAW_ERR = 3;  //提现失败

    /**
     * 系统配置常量类型
     */
    public static final int CONFIG_APP_START_IMG = 1;   //App启动页配置类型
    public static final  int WEB_CHINESE_CHATROOM_PERSON_COUNT = 2; //APP活动聊天室最少人数

    public static final String ZHI_HE_TECH = "挚合实淘";


    /**
     * 支付宝订单（ping++订单号）
     */
    public static final String BEGIN_CODE_PINGPP_ORDER = "P861";

    /**
     * 商品订单编号的前三位
     */
    public static final String BEGIN_CODE_ORDER = "Z860";

    /**
     * 活动费用订单单前三位
     */
    public static final String BEGIN_CODE_ACTIVITY_ORDER = "862";

    /**
     * 商家结算账单的前三位
     */
    public static final String BEGIN_CODE_MERCHANT_BILL = "886";

    /**
     * 优惠券码的前三位
     */
    public static final String BEGIN_CODE_COUPON = "080";

    /**
     * 抽奖活动幸运码的前三位
     */
    public static final String BEGIN_CODE_LUCKY_DRAW = "081";
}
