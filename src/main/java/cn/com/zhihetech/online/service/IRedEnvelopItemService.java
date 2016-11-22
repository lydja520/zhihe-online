package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by ShenYunjie on 2015/12/14.
 */
public interface IRedEnvelopItemService extends SupportService<RedEnvelopItem> {
    /**
     * 根据红包信息自动分配单个红包金额
     *
     * @param redEnvelop
     */
    void addRedEnvelopItemByRedEvelop(RedEnvelop redEnvelop);

    /**
     * 根据红包删除对应的红包项
     *
     * @param redEnvelop
     */
    void deleteByRedEnvelop(RedEnvelop redEnvelop);
}
