package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.CouponItem;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.dao.ICouponItemDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.ICouponItemService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.SyncFailedException;
import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/3/9.
 */
@Service(value = "couponItemService")
public class CouponItemServiceImpl implements ICouponItemService {

    @Resource(name = "couponItemDao")
    private ICouponItemDao couponItemDao;

    @Override
    public CouponItem getById(String id) {
        return this.couponItemDao.findEntityById(id);
    }

    @Override
    public void delete(CouponItem couponItem) {

    }

    @Override
    public CouponItem add(CouponItem couponItem) {
        return this.couponItemDao.saveEntity(couponItem);
    }

    @Override
    public void update(CouponItem couponItem) {
        this.couponItemDao.updateEntity(couponItem);
    }

    @Override
    public List<CouponItem> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.couponItemDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<CouponItem> getPageData(Pager pager, IQueryParams queryParams) {
        return this.couponItemDao.getPageData(pager, queryParams);
    }

    @Override
    public CouponItem executeUseCoupon(Admin admin, String couponCode, String userPhone) {
        if (StringUtils.isEmpty(couponCode) || StringUtils.isEmpty(userPhone)) {
            throw new SystemException("查询失败，请输入完整的查询条件！");
        }
        Merchant merchant = admin.getMerchant();
        IQueryParams queryParams = new GeneralQueryParams();
        if (merchant != null) {
            queryParams.andEqual("coupon.merchant", merchant);
        }
        queryParams.andEqual("code", couponCode).andEqual("user.userPhone", userPhone);
        List<CouponItem> couponItems = this.couponItemDao.getEntities(null, queryParams);
        if (couponItems.size() <= 0) {
            throw new SystemException("对不起！不存在该优惠券");
        }
        CouponItem couponItem = couponItems.get(0);
        if (couponItem.isUseState()) {
            throw new SystemException("此优惠券已经使用过！");
        }
        Date now = new Date();
        if (now.before(couponItem.getBeginValidity())) {
            throw new SystemException("此优惠券未到使用时间！优惠券的有效期是 " + couponItem.getBeginValidity() + " 至 " + couponItem.getValidity());
        }
        if (now.after(couponItem.getValidity())) {
            throw new SystemException("此优惠券已经过期！优惠券的有效期是" + couponItem.getBeginValidity() + " 至 " + couponItem.getValidity());
        }
        couponItem.setUseState(true);
        couponItem.setUseDate(now);
        this.couponItemDao.updateEntity(couponItem);
        return couponItem;
    }
}
