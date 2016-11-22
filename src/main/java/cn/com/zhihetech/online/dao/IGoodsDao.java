package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.vo.GoodsParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/14.
 */
public interface IGoodsDao extends SupportDao<Goods> {

    /**
     * 批量添加商品的销量
     *
     * @param goodsIds    商品ID列表
     * @param goodsCounts 商品购买购买的数量列表
     */
    void executeAddVolumeByGoodsIdsAndGoodsCounts(List<String> goodsIds, List<Long> goodsCounts);

    /**
     * 减去商品的销量
     *
     * @param goodsId    商品ID
     * @param goodsCount 要减少的销量数
     */
    void executeSubGoodsVolume(String goodsId, long goodsCount);

    /**
     * 增加商品的销量
     *
     * @param goodsId 商品ID
     * @param volume  要增加的销量数
     */
    void executeAddGoodsVolume(String goodsId, long volume);

    /**
     * 根据请求参数获取商品列表,返回的商品需要调用setTransientInfo(...)方法
     *
     * @param page   分页
     * @param params 查询参数
     * @return
     */
    PageData<Goods> getGoodsPageDataByParams(Pager page, GoodsParams params);
}
