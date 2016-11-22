package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.GoodsBrowse;
import cn.com.zhihetech.online.dao.IGoodsBrowseDao;
import cn.com.zhihetech.online.service.AbstractService;
import cn.com.zhihetech.online.service.IGoodsBrowseService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/18.
 */
@Service("goodsBrowseService")
public class GoodsBrowseServiceImpl extends AbstractService<GoodsBrowse> implements IGoodsBrowseService {

    @Resource(name = "goodsBrowseDao")
    private IGoodsBrowseDao goodsBrowseDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public GoodsBrowse getById(String id) {
        return this.goodsBrowseDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param goodsBrowse 需要删除的持久化对象
     */
    @Override
    public void delete(GoodsBrowse goodsBrowse) {
        this.goodsBrowseDao.deleteEntity(goodsBrowse);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param goodsBrowse 需要持久化的对象
     * @return
     */
    @Override
    public GoodsBrowse add(GoodsBrowse goodsBrowse) {
        if (goodsBrowse.getBrowseDate() == null) {
            goodsBrowse.setBrowseDate(new Date());
        }
        return this.goodsBrowseDao.saveEntity(goodsBrowse);
    }

    /**
     * 更新一个持久化对象
     *
     * @param goodsBrowse
     */
    @Override
    public void update(GoodsBrowse goodsBrowse) {
        this.goodsBrowseDao.updateEntity(goodsBrowse);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<GoodsBrowse> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.goodsBrowseDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<GoodsBrowse> getPageData(Pager pager, IQueryParams queryParams) {
        return this.goodsBrowseDao.getPageData(pager, queryParams);
    }
}
