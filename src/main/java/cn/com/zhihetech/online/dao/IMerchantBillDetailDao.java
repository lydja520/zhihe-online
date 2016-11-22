package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.MerchantBillDetail;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/5/9.
 */
public interface IMerchantBillDetailDao extends SupportDao<MerchantBillDetail> {

    List<String> getMerchantIds(Date startDate, Date endDate);
}
