package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Navigation;
import cn.com.zhihetech.online.dao.INavigationDao;
import cn.com.zhihetech.online.service.INavigationService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2015/12/21.
 */
@Service(value = "navigationService")
public class NavigationServiceImpl implements INavigationService {

    @Resource(name = "navigationDao")
    private INavigationDao navigationDao;

    @Override
    public Navigation getById(String id) {
        return this.navigationDao.findEntityById(id);
    }

    @Override
    public void delete(Navigation navigation) {

    }

    @Override
    public Navigation add(Navigation navigation) {
        return this.navigationDao.saveEntity(navigation);
    }

    @Override
    public void update(Navigation navigation) {
        this.navigationDao.updateEntity(navigation);
    }

    @Override
    public List<Navigation> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.navigationDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<Navigation> getPageData(Pager pager, IQueryParams queryParams) {
        return this.navigationDao.getPageData(pager, queryParams);
    }
}
