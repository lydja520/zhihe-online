package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.MerchantAdv;
import cn.com.zhihetech.online.dao.IMerchantAdvDao;
import cn.com.zhihetech.online.service.AbstractService;
import cn.com.zhihetech.online.service.IMerchantAdvService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/22.
 */
@Service("merchantAdvService")
public class MerchantAdvServiceImpl extends AbstractService<MerchantAdv> implements IMerchantAdvService {

    @Resource(name = "merchantAdvDao")
    private IMerchantAdvDao merchantAdvDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public MerchantAdv getById(String id) {
        return this.merchantAdvDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param merchantAdv 需要删除的持久化对象
     */
    @Override
    public void delete(MerchantAdv merchantAdv) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("advId", merchantAdv.getAdvId());
        this.merchantAdvDao.executeDelete(queryParams);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param merchantAdv 需要持久化的对象
     * @return
     */
    @Override
    public MerchantAdv add(MerchantAdv merchantAdv) {
        if (merchantAdv.getCreateDateTime() == null) {
            merchantAdv.setCreateDateTime(new Date());
        }
        return this.merchantAdvDao.saveEntity(merchantAdv);
    }

    /**
     * 更新一个持久化对象
     *
     * @param merchantAdv
     */
    @Override
    public void update(MerchantAdv merchantAdv) {
        if (merchantAdv.getLocation() == null) {
            merchantAdv.setLocation(MerchantAdv.AdvLocation.anywhere);
        }
        this.merchantAdvDao.updateEntity(merchantAdv);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<MerchantAdv> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.merchantAdvDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<MerchantAdv> getPageData(Pager pager, IQueryParams queryParams) {
        return this.merchantAdvDao.getPageData(pager, queryParams);
    }
}
