package cn.com.zhihetech.online.v2.service;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.vo.GoodsParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;

/**
 * Created by ShenYunjie on 2016/7/13.
 */
public interface IGoodsService {
    /**
     * 根据分页参数和查询参数查询商家
     *
     * @param pager
     * @param params
     * @return
     */
    PageData<Goods> getPageDataByParams(Pager pager, GoodsParams params);
}
