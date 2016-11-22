package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.FocusMerchant;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

import java.util.Date;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
public interface IFocusMerchantDao extends SupportDao<FocusMerchant> {
    List<String> getInFiveDayBrithdayFocusMerchant(Date now);
}
