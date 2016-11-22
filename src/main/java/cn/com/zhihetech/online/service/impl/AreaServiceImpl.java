package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.dao.impl.AreaDaoImpl;
import cn.com.zhihetech.online.service.IAreaService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yandaichun on 2015/11/23.
 */
@Service("areaService")
public class AreaServiceImpl implements IAreaService{

    @Resource(name = "areaDao")
    private AreaDaoImpl areaDao;

    @Override
    public Area getById(String id) {
        return this.areaDao.findEntityById(id);
    }

    @Override
    public void delete(Area area) {
        this.areaDao.deleteEntity(area);
    }

    @Override
    public Area add(Area area) {
       return this.areaDao.saveEntity(area);
    }

    @Override
    public void update(Area area) {
        this.areaDao.updateEntity(area);
    }

    @Override
    public List<Area> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.areaDao.getEntities(pager,queryParams);
    }

    @Override
    public PageData<Area> getPageData(Pager pager, IQueryParams queryParams) {
        return this.areaDao.getPageData(pager,queryParams);
    }
}
