package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.FocusMerchant;
import cn.com.zhihetech.online.dao.IFocusMerchantDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/12/15.
 */
@Repository("focusMerchantDao")
public class IFocusMerchantDaoImpl extends SimpleSupportDao<FocusMerchant> implements IFocusMerchantDao {
    @Override
    public List<String> getInFiveDayBrithdayFocusMerchant(Date now) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String nowYear = simpleDateFormat.format(now);
        String sql = "select d.focus_id from "+
                "(select c.* from " +
                "(select a.* from " +
                "(select * from t_user where CAST(CONCAT('"+nowYear+"',DATE_FORMAT(user_birthday,'-%m-%d')) AS DATE)-CAST(CURRENT_DATE() AS DATE)>=0)a " +
                "where CAST(CONCAT('"+nowYear+"',DATE_FORMAT(a.user_birthday,'-%m-%d')) AS DATE)-CAST(CURRENT_DATE() AS DATE)<=5) b " +
                "LEFT JOIN t_focus_merchant c on b.user_id = c.user_id) d " +
                "WHERE d.user_id!='' GROUP BY d.user_id;";
        Session session = this.getSession();
        Query query = session.createSQLQuery(sql);
        List<String> focusMerchantIds = query.list();
        return focusMerchantIds;
    }
}
