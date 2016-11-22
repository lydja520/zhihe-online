package cn.com.zhihetech.online.service;

import com.sun.istack.internal.NotNull;

/**
 * Created by ShenYunjie on 2016/7/12.
 */
public interface IExamineGoodsService {

    /**
     * 提交商品审核
     *
     * @param goodsId
     */
    void executeSubmitGoodsExamine(@NotNull String goodsId);

    /**
     * 审核商品（商品审核通过）
     *
     * @param goodsId
     */
    void executeExaminedGoods(String goodsId);

    /**
     * 商品审核未通过
     *
     * @param goodsId 商品ID
     * @param msg     审核信息
     */
    void executeUnExaminedGoods(String goodsId, String msg);
}
