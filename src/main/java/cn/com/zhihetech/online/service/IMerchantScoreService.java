package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.MerchantScore;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by YangDaiChun on 2016/2/18.
 */
public interface IMerchantScoreService extends SupportService<MerchantScore> {
    Float getMerchantScoreByMerchantId(String merchantId);
}
