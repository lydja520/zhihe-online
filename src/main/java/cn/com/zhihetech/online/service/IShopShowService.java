package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ShopShow;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/1/11.
 */
public interface IShopShowService extends SupportService<ShopShow> {

    void saveOrUpdateShopShow(String merchantId, String... shopShows);

    /**
     * 获取指定商家的门店照（最多只能获取20张照片）
     *
     * @param merchantId
     * @return
     */
    List<ShopShow> getShopShowsByMerchantId(String merchantId);

    long getRecordTotal(IQueryParams queryParams);
}
