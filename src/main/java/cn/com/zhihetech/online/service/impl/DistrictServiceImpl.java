package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.District;
import cn.com.zhihetech.online.dao.IDistrictDao;
import cn.com.zhihetech.online.service.IDistrictService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/20.
 */
@Service("districtService")
public class DistrictServiceImpl implements IDistrictService {
    @Resource(name = "districtDao")
    private IDistrictDao districtDao;

    @Override
    public District getById(String id) {
        return this.districtDao.findEntityById(id);
    }

    @Override
    public void delete(District district) {
        this.districtDao.deleteEntity(district);
    }

    @Override
    public District add(District district) {
        return this.districtDao.saveEntity(district);
    }

    @Override
    public void update(District district) {
        this.districtDao.updateEntity(district);
    }

    @Override
    public List<District> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.districtDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<District> getPageData(Pager pager, IQueryParams queryParams) {
        return this.districtDao.getPageData(pager, queryParams);
    }
}
