package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/3/4.
 */
public interface IActivityGoodsService extends SupportService<ActivityGoods> {

    void addActivityGoods(ActivityGoods activityGoods);

    //TODO:得到每一个商家的活动商品
    Map<String, List<ActivityGoods>> getActivityGoods(Pager pager, List<Merchant> merchants, String activityId);

    /**
     * 根据活动ID或管理员（商家）ID获取秒杀商品列表 （为了兼容Android App1.0.4中传递的merchantId为adminId的情况）
     *
     * @param pager
     * @param acitivitId 活动ID
     * @param adminOrMerchantId 管理员（商家）ID
     * @return
     */
    PageData<ActivityGoods> getActivityGoodsByAdminIdOrMerchantId(Pager pager, String acitivitId, String adminOrMerchantId);

    float getActivityGoodsPriceById(String activityGodosId);

    List<Object> getProperty(String s, Pager pager, IQueryParams queryParams);
}
