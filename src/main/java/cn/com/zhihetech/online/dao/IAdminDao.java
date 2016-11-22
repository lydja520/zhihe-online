package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.Menu;
import cn.com.zhihetech.online.bean.Role;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
public interface IAdminDao extends SupportDao<Admin> {
    /**
     * 根据管理员ID获取管理员权限菜单
     *
     * @param adminId
     * @return
     */
    List<Menu> getMenusByAdminId(String adminId);
}
