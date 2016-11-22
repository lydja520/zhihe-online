package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.LuckyDrawActivity;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ydc on 16-4-21.
 */
public interface ILuckyDrawActivityService extends SupportService<LuckyDrawActivity> {

    void executeOnlineOP(String activityId, boolean permit);

    /**
     * 获取获取此次抽奖活动的赞助商家
     *
     * @return
     */
    List<Merchant> getSponsorMerchants();

    /**
     * 获取当前正在进行的大转盘活动
     *
     * @return
     */
    LuckyDrawActivity getCurrentLuckDrawActivity();
}
