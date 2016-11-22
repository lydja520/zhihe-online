package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/5/17.
 */
public interface IVipZoneService {
    PageData<Merchant> getVipMerchants(Pager pager);
}
