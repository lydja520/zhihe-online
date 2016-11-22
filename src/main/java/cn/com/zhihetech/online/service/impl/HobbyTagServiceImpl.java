package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.HobbyTag;
import cn.com.zhihetech.online.dao.IHobbyTagDao;
import cn.com.zhihetech.online.service.IHobbyTagService;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/1/13.
 */
@Service("hobbyTagService")
public class HobbyTagServiceImpl implements IHobbyTagService {

    @Resource(name = "hobbyTagDao")
    private IHobbyTagDao hobbyTagDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public HobbyTag getById(String id) {
        return this.hobbyTagDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param hobbyTag 需要删除的持久化对象
     */
    @Override
    public void delete(HobbyTag hobbyTag) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("tagId", hobbyTag.getTagId());
        this.hobbyTagDao.executeDelete(queryParams);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param hobbyTag 需要持久化的对象
     * @return
     */
    @Override
    public HobbyTag add(HobbyTag hobbyTag) {
        if (hobbyTag.getParentTag() != null && StringUtils.isEmpty(hobbyTag.getParentTag().getTagId())) {
            hobbyTag.setParentTag(null);
        }
        return this.hobbyTagDao.saveEntity(hobbyTag);
    }

    /**
     * 更新一个持久化对象
     *
     * @param hobbyTag
     */
    @Override
    public void update(HobbyTag hobbyTag) {
        this.hobbyTagDao.updateEntity(hobbyTag);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<HobbyTag> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.hobbyTagDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<HobbyTag> getPageData(Pager pager, IQueryParams queryParams) {
        return this.hobbyTagDao.getPageData(pager, queryParams);
    }

    @Override
    public List<HobbyTag> getTagsByParentId(String parentId) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("parentTag.tagId", parentId);
        return this.hobbyTagDao.getEntities(null, queryParams);
    }
}
