package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.MerchantBill;
import cn.com.zhihetech.online.dao.IMerchantBillDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by YangDaiChun on 2016/4/8.
 */
@Repository(value = "merchantBillDao")
public class MerchantBillDaoImpl extends SimpleSupportDao<MerchantBill> implements IMerchantBillDao {

    @Override
    public long getThisWeekDataAmount(Merchant merchant, Date startDate, Date endDate) {
        Session session = this.getSession();
        String hql = "SELECT COUNT(*) FROM MerchantBill AS a WHERE a.merchant =:merchant AND a.createDate >=:startDate AND a.createDate <=:endDate";
        Query query = session.createQuery(hql);
        query.setEntity("merchant", merchant);
        query.setTimestamp("startDate", startDate);
        query.setTimestamp("endDate", endDate);
        return (long) query.uniqueResult();
    }

}
