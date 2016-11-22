package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.MerchantBill;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

import java.util.Date;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
public interface IMerchantBillDao extends SupportDao<MerchantBill> {
    long getThisWeekDataAmount(Merchant merchant, Date startDate, Date endDate);
}
