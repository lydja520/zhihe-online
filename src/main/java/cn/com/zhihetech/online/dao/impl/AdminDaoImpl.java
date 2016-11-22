package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.Menu;
import cn.com.zhihetech.online.dao.IAdminDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
@Repository("adminDao")
public class AdminDaoImpl extends SimpleSupportDao<Admin> implements IAdminDao {
    @Override
    public List<Menu> getMenusByAdminId(String adminId) {
        Map<String, Object> values = new HashMap<>();
        values.put("adminId", adminId);
        StringBuilder hql = new StringBuilder("select distinct root from Admin ad join ad.roles r join r.menus m join m.parentMenu root")
                .append(" where ad.adminId = :adminId and ad.permit = true and m.permit = true and root.parentMenu is null order by root.menuOrder");
        Query query = createQueryWithHQL(hql.toString());
        initQueryValues(query, values);
        List<Menu> roots = query.list();

        hql = new StringBuilder("select distinct m from Admin ad join ad.roles r join r.menus m")
                .append(" where ad.adminId = :adminId and m.permit = true and m.parentMenu is not null order by m.menuOrder");
        query = createQueryWithHQL(hql.toString());
        initQueryValues(query, values);
        List<Menu> menus = query.list();

        if (roots != null && menus != null) {
            for (Menu root : roots) {
                for (Menu menu : menus) {
                    if (menu.getParentMenu().getMenuId().equals(root.getMenuId())) {
                        root.getChildList().add(menu);
                    }
                }
            }
        }
        return roots;
    }
}
