package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.LuckyDraw;
import cn.com.zhihetech.online.dao.ILuckyDrawActivityDao;
import cn.com.zhihetech.online.dao.ILuckyDrawDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ydc on 16-4-22.
 */
@Repository(value = "luckyDrawDao")
public class LuckyDrawDaoImpl  extends SimpleSupportDao<LuckyDraw> implements ILuckyDrawDao{
    @Override
    public List<LuckyDraw> getMinLuckDraw() {
        Session session= this.getSession();
        String hql = "from LuckyDraw as c where c.percentage in (SELECT min(b.percentage) from LuckyDraw as b where b.ldOrder <> 0)";
        Query query = session.createQuery(hql);
        List<LuckyDraw> luckyDraws = query.list();
        return luckyDraws;
    }
}
