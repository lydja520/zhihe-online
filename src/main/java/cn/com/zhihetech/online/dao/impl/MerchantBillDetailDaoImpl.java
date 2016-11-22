package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.MerchantBillDetail;
import cn.com.zhihetech.online.dao.IMerchantBillDetailDao;
import cn.com.zhihetech.online.util.DateUtils;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/5/9.
 */
@Repository(value = "merchantBillDetailDao")
public class MerchantBillDetailDaoImpl extends SimpleSupportDao<MerchantBillDetail> implements IMerchantBillDetailDao {
    @Override
    public List<String> getMerchantIds(Date startDate, Date endDate) {
        Session session = this.getSession();
        StringBuffer sqlBuffer = new StringBuffer("SELECT DISTINCT c.merch_id FROM t_merchant_bill_detail AS a,t_order AS b,t_merchant AS c ")
                .append("WHERE a.order_id = b.order_id ")
                .append("AND b.merchant_id = c.merch_id ")
                .append("AND a.create_date >= :startDate ")
                .append("AND a.create_date <= :endDate ")
                .append("AND a.handle_state=FALSE");
        Query query = session.createSQLQuery(sqlBuffer.toString());
        query.setTimestamp("startDate", startDate);
        query.setTimestamp("endDate", endDate);
        return query.list();
    }
}
