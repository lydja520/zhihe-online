package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by YangDaiChun on 2015/12/4.
 */
public interface IGoodsAttributeSetService extends SupportService<GoodsAttributeSet> {

    /**
     * 更新是否可用
     *
     * @param attSetId
     * @param permit
     */
    void updatePermit(String attSetId, boolean permit);

    /**
     * 获取指定商品的属性总数
     *
     * @param goodsId
     * @return
     */
    int getSkuAttrCountByGoodsId(String goodsId);
}
