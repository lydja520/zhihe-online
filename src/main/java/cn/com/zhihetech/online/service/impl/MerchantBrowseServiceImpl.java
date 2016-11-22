package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.MerchantBrowse;
import cn.com.zhihetech.online.dao.IMerchantBrowseDao;
import cn.com.zhihetech.online.service.AbstractService;
import cn.com.zhihetech.online.service.IMerchantBrowseService;
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
@Service("merchantBrowseService")
public class MerchantBrowseServiceImpl extends AbstractService<MerchantBrowse> implements IMerchantBrowseService {

    @Resource(name = "merchantBrowseDao")
    private IMerchantBrowseDao merchantBrowseDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public MerchantBrowse getById(String id) {
        return this.merchantBrowseDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param merchantBrowse 需要删除的持久化对象
     */
    @Override
    public void delete(MerchantBrowse merchantBrowse) {
        this.merchantBrowseDao.deleteEntity(merchantBrowse);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param merchantBrowse 需要持久化的对象
     * @return
     */
    @Override
    public MerchantBrowse add(MerchantBrowse merchantBrowse) {
        if (merchantBrowse.getBrowseDate() == null) {
            merchantBrowse.setBrowseDate(new Date());
        }
        return this.merchantBrowseDao.saveEntity(merchantBrowse);
    }

    /**
     * 更新一个持久化对象
     *
     * @param merchantBrowse
     */
    @Override
    public void update(MerchantBrowse merchantBrowse) {
        this.merchantBrowseDao.updateEntity(merchantBrowse);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<MerchantBrowse> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.merchantBrowseDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<MerchantBrowse> getPageData(Pager pager, IQueryParams queryParams) {
        return this.merchantBrowseDao.getPageData(pager, queryParams);
    }
}
