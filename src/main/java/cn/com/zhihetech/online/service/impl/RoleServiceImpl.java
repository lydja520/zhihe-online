package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Admin;
import cn.com.zhihetech.online.bean.Menu;
import cn.com.zhihetech.online.bean.Role;
import cn.com.zhihetech.online.dao.IAdminDao;
import cn.com.zhihetech.online.dao.IRoleDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IAdminService;
import cn.com.zhihetech.online.service.IMenuService;
import cn.com.zhihetech.online.service.IRoleService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
@Service("roleService")
public class RoleServiceImpl implements IRoleService {
    @Resource(name = "roleDao")
    private IRoleDao roleDao;
    @Resource(name = "menuService")
    private IMenuService menuService;
    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "adminDao")
    private IAdminDao adminDao;

    @Override
    public Role getById(String id) {
        return this.roleDao.findEntityById(id);
    }

    @Override
    public void delete(Role role) {
        this.roleDao.deleteEntity(role);
    }

    @Override
    public Role add(Role role) {
        return this.roleDao.saveEntity(role);
    }

    @Override
    public void update(Role role) {
        this.roleDao.updateEntity(role);
    }

    @Override
    public List<Role> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.roleDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<Role> getPageData(Pager pager, IQueryParams queryParams) {
        return this.roleDao.getPageData(pager, queryParams);
    }

    @Override
    public Role saveOrUpdate(Role role) {
        return this.roleDao.saveOrUpdate(role);
    }
}
