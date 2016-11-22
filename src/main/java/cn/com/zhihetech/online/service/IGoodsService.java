package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.GoodsBanner;
import cn.com.zhihetech.online.bean.GoodsDetail;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.vo.GoodsAndSku;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;
import com.sun.istack.internal.NotNull;

import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/14.
 */
public interface IGoodsService extends SupportService<Goods> {

    /**
     * 逻辑删除商品
     *
     * @param id
     */
    void updateDeleteState(String id);

    /**
     * 更新上下架状态
     *
     * @param id
     * @param onsal
     */
    void updateOnsalState(String id, boolean onsal);

    /**
     * 添加商品
     *
     * @param goods
     * @param goodsBanners
     * @param goodsDetails
     */
    Goods addOrUpdate(Goods goods, List<GoodsBanner> goodsBanners, List<GoodsDetail> goodsDetails);

    /**
     * 根据商家ID获取可推荐的商品
     *
     * @param queryParams
     * @param pager
     * @param merchantId
     * @return
     */
    PageData<Goods> getAbleRecommendGoodsesByMerchantId(IQueryParams queryParams, Pager pager, String merchantId);

    ResponseMessage addOrUpdateGoods(Goods goods, List<GoodsBanner> goodsBanners, List<GoodsDetail> goodsDetails, Merchant merchant);

    /**
     * 下架商品
     *
     * @param goodsId
     */
    void executeOffLineGoods(String goodsId);

    /**
     * 上架商品
     *
     * @param goodsId
     */
    void executeOnSaleGoods(String goodsId);

    /**
     * 提交商品审核
     *
     * @param goodsId
     */
    void executeSubmitExamine(String goodsId);

    /**
     * 审核商品并审核通过
     *
     * @param goodsId 商品ID
     */
    void executeExamineGoods(String goodsId);

    /**
     * 商品审核未通过
     *
     * @param goodsId 商品ID
     * @param msg     审核未通过原因
     */
    void executeUnExamineGoods(String goodsId, String msg);

    /**
     * 批量更新商品的销量
     *
     * @param goodsIds    商品ID列表
     * @param goodsCounts 商品购买购买的数量列表
     */
    void executeAddVolumeByGoodsIdsAndGoodsCounts(List<String> goodsIds, List<Long> goodsCounts);

    /**
     * 根据商品ID获取商品的状态
     *
     * @return
     * @par am goodsId
     */
    Integer getGoodState(String goodsId);

    /**
     * 将指定商品的状态更新为未提交审核
     *
     * @param goodsId
     */
    void executeUpdateGoodsNoSubmitExamine(@NotNull String goodsId);

    /**
     * 根据指定商品ID获取商品是否已上架
     *
     * @param goodsId
     * @return
     */
    boolean isOnSale(String goodsId);

    List<GoodsAndSku> getGoodsAndSkus(List<Goods> goodses, String goodsId);

    List<Object> getProperty(String s, Pager pager, IQueryParams queryParams);

    /**
     * 更新商品的库存、销量、最小价格和最大价格信息
     *
     * @param goodsId
     */
    void executeUpdateGoodsTransientInfo(String goodsId);

    String getMerchantId(String goodsId);
}
