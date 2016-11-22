package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.FocusMerchant;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
public interface IFocusMerchantService extends SupportService<FocusMerchant> {


    PageData<Merchant> getMerchantPageData(Pager pager,IQueryParams queryParams);

    //TODO:得到每一个活动商家的关注用户
    Map<String,List<FocusMerchant>> getMerchantFocusUser(Pager pager,List<Merchant> merchantId,String activityId);

}
