package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.GoodsScore;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by YangDaiChun on 2016/2/18.
 */
public interface IGoodsScoreService extends SupportService<GoodsScore> {
    /**
     * 根据商品ID获取商品的评论
     *
     * @param goodsId
     * @param pager
     * @return
     */
    PageData<GoodsScore> getGoodsScoresByGoodsId(String goodsId, Pager pager);

    List<GoodsScore> getGoodsScores(String goodsId);

    List<GoodsScore> getGoodsScoreBygoodsId(String merchantId,String goodsId, Pager pager, IQueryParams queryParams);

    /**
     * 分页查询评论详细信息
     * @param pager 分页信息
     * @param queryParams   查询参数
     * @return
     */
    PageData<GoodsScore> getAllPageData(Pager pager, IQueryParams queryParams);
}
