package cn.com.zhihetech.online.commons;

import java.io.Serializable;

/**
 * 返回状态码
 * Created by ShenYunjie on 2015/11/18.
 */
public class ResponseStatusCode implements Serializable, Cloneable {
    /**
     * 正常返回
     */
    public final static int SUCCESS_CODE = 200;
    /**
     * 系统内部错误
     */
    public final static int SYSTEM_ERROR_CODE = 500;
    /**
     * 页面未找到
     */
    public final static int PAGE_NOT_FOUND_CODE = 404;

    /**
     * 未授权
     */
    public final static int UNAUTHORIZED = 401;


    /**
     * 商品已经下架
     */
    public final static int NOT_ONSAL = 610;

    /**
     * 商品卖完了
     */
    public final static int NO_STOCK = 620;

    /**
     * 商品库存量不足
     */
    public final static int NOT_ENOUGH_STOCK = 630;

    /**
     * 秒杀商品重复下单
     */
    public final static int REPEAT_SECKILL_ORDER = 640;

    /**
     * 秒杀商品已经被抢光啦
     */
    public final static int ALREADY_DRAW_OVER = 645;

    /**
     * 已经关注（收藏过）
     */
    public final static int ALREADY_FOCUS = 710;

    /**
     * 未关注（收藏过）
     */
    public final static int HAVE_NO_FOCUS = 715;

    /**
     * 关注（收藏）为空
     */
    public final static int NOT_FOCUS = 720;

    /**
     * 当前活动状态不支持加入操作
     */
    public final static int FORBID_ADD = 810;

    /**
     * 添加活动失败，请稍后再试
     */
    public final static int CHATROOM_ADD_SINGLE_USER_FAILURE = 815;

    /**
     * 用户已经加入到该活动中，无需再次加入
     */
    public final static int USER_ALREADY_IN_ACTIVITY_FANS = 820;

    /**
     * 红包已经被抢光
     */
    public final static int RED_ENVELOP_FINISHED = 850;

    /**
     * 已经领取过红包
     */
    public final static int RED_ENVELOP_ALREADY_RECEIVED = 855;

    /**
     * 优惠券已抢过
     */
    public final static int COUPON_ALREADY_GRAB = 861;

    /**
     * 优惠券抢光了
     */
    public final static int COUPON_ALREADY_OVER = 862;

    /**
     * 未中奖
     */
    public final static int NOTLUCK = 900;

    /**
     * 奖品已经被抽完了
     */
    public final static int LUCKDRAW_ALREADY_OVER = 901;

    /**
     * 你已经抽过奖了
     */
    public final static int ALREADY_LUCKDRAW = 902;

    /**
     * 不存在抽奖活动
     */
    public final static int HAVE_NO_LUCKY_DRAW = 910;

}
