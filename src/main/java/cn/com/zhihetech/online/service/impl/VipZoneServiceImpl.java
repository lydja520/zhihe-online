package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.dao.IMerchantDao;
import cn.com.zhihetech.online.service.IVipZoneService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.Order;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by ShenYunjie on 2016/5/17.
 */
@Service("vipZoneService")
public class VipZoneServiceImpl implements IVipZoneService {
    @Resource(name = "merchantDao")
    private IMerchantDao merchantDao;

    /**
     * 获取VIP店铺（只获取商家的名称和ID)
     *
     * @param pager
     * @return
     */
    @Override
    public PageData<Merchant> getVipMerchants(Pager pager) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("storeType", Merchant.StoreType.vipStore)
                .andEqual("examinState", Constant.EXAMINE_STATE_EXAMINED_OK)
                .andEqual("permit", true)
                .sort("createDate", Order.DESC);
        return this.merchantDao.getPageData(pager, queryParams);
    }
}
