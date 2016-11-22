package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.vo.GoodsTransientInfo;

/**
 * Created by ShenYunjie on 2016/7/12.
 */
public interface IGoodsTransientInfoService {

    /**
     *
     * @param goodsId
     * @return
     */
    GoodsTransientInfo getGoodsTransientInfoByGoodsId(String goodsId);
}
