package cn.com.zhihetech.online.commons;

import cn.com.zhihetech.online.bean.SerializableAndCloneable;

/**
 * 网建短信配置
 * Created by YangDaiChun on 2015/12/18.
 */
public class WebChineseConfig extends SerializableAndCloneable {

    public static final String UID = "zhihetech";
    public static final String KEY = "a8d3eaa609d37d37fd39";

    public static final int VER_CODE_VALID = 1000 * 60 * 10;   //验证码有效期，10分钟有效

    public class MsgTemplate {
        /**
         * 发货提醒模板
         */
        public final static String SEND_GOODS_REMIND = "尊敬的商家，您好！{0}您的实淘店铺订单{1}已支付。请尽快安排配送，如已处理，可忽略此提醒。【实淘】";
        /**
         * 注册验证码模板
         */
        public final static String REGISTER_VER_CODE = "尊敬的用户，您好！欢迎您注册实淘，为了确保您信息的真实性，您本次操作的验证码是{0}，有效时间为1分钟。请在页面填写验证码完成验证。(如非本人操作，可不予理会)。【实淘】";

        /**
         * 找回密码验证码
         */
        public final static String FIND_PASSWORD_VER_CODE = "尊敬的用户，您正通过实淘账号找回密码，验证码是{0}，有效时间为1分钟。感谢您对实淘的支持。【实淘】";

        /**
         * 发送新密码
         */
        public final static String SEND_NEW_PASS_WORD = "尊敬的用户，系统已为您重置密码，新的密码为{0}，为保证账号安全，请登录后在“我的”选项中重新设置密码，感谢您对实淘的支持。【实淘】";

        /**
         * 活动邀请
         */
        public final static String ACTIVITY_INVITATION = "尊敬的商家，您好！{0}店铺正邀请您参与实淘会客厅，请及时联系沟通，联系人{1}，联系电话{2}，感谢您对实淘的支持。【实淘】";

        /**
         * 申请提现
         */
        public final static String APPLY_TAKE_CASH = "尊敬的用户，您正通过实淘账号提取现金，验证码是{0}，有效时间为1分钟。感谢您对实淘的支持。【实淘】";

        /**
         * 用户提现成功提醒
         */
        public final static String USER_WITHDRAW_OK = "尊敬的用户，您好！实淘已经处理了你的提现申请，请及时登录APP进行核实，如有问题，请尽快联系实淘进行处理，感谢您对实淘的支持。【实淘】";

        /**
         * 提醒商家有用户申请退款
         */
        public final static String ORDER_REFUND_WITHDRAW = "尊敬的商家，您好！{0}时您的实淘店铺订单{1}申请退款。请尽快处理，如已处理，可忽略此提醒。【实淘】";

        /**
         * 提醒商家注册已通过审核
         */
        public final static String MERCHANT_EXAMIN_OK = "尊敬的商家，您好！你提交的注册实淘商家申请于{0}通过实淘官方审核，请登录实淘后台管理系统进行确认，感谢您对实淘的支持。【实淘】";

        /**
         * 活动开始30分钟前提醒
         */
        public final static String ACTIVITY_START_REMINDER = "尊敬的用户，您好！您关注的商家将在30分钟后{0}举办实淘会客厅，您心仪的商品将会进行低价秒杀，更有现金红包等你来抢！请准时参加哦~ 退订回复TD【实淘】";

        /**
         * 系统自动确认收货提示
         */
        public final static String ORDER_CONFIRM_RECEIVE = "尊敬的用户，您好！你在实淘购买的订单号为{0}的订单由于长时间未确认收货，系统将于两天后自动确认收货，请尽快到实淘APP进行确认收货操作，如果该订单出现什么问题，请到APP投诉维权中及时联系实淘工作人员进行处理，感谢您对实淘的支持【实淘】";

        /**
         * 商家账单已结算通知
         */
        public final static String ORDER_ALREADY_BILL = "尊敬的商家，您好！您的实淘账单{0}已结算，请注意查收！如有异议，请及时与实淘联系，感谢您对实淘的支持。【实淘】";

        /**
         * 订单退款成功提醒
         */
        public static final String ORDER_REFUND_OK = "尊敬的用户，您好！您申请的订单号为：{0} 的退款，实淘已经为您处理，请注意查收！【实淘】";

    }

}
