package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Menu;
import cn.com.zhihetech.online.dao.IMenuDao;
import cn.com.zhihetech.online.service.IMenuService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
@Service("menuService")
public class MenuServiceImpl implements IMenuService {

    @Resource(name = "menuDao")
    protected IMenuDao menuDao;

    /**
     * 根据ID获取持久化对象
     * @param id 对象ID
     * @return
     */
    @Override
    public Menu getById(String id) {
        return menuDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     * @param menu 需要删除的持久化对象
     */
    @Override
    public void delete(Menu menu) {
        this.menuDao.deleteEntity(menu);
    }

    /**
     * 添加一个对象到数据库
     * @param menu  需要持久化的对象
     * @return
     */
    @Override
    public Menu add(Menu menu) {
        return this.menuDao.saveEntity(menu);
    }

    /**
     * 更新一个持久化对象
     * @param menu
     */
    @Override
    public void update(Menu menu) {
        this.menuDao.updateEntity(menu);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     * @param pager 分页参数
     * @param queryParams   查询条件
     * @return
     */
    @Override
    public List<Menu> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.menuDao.getEntities(pager,queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     * @param pager 分页参数
     * @param queryParams   查询条件
     * @return  经过分页的数据
     */
    @Override
    public PageData<Menu> getPageData(Pager pager, IQueryParams queryParams) {
        return this.menuDao.getPageData(pager,queryParams);
    }
}
