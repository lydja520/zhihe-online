package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.online.dao.IRedEnvelopItemServiceDao;
import cn.com.zhihetech.online.service.IRedEnvelopItemService;
import cn.com.zhihetech.online.util.RedEnvelopUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/12/14.
 */
@Service("redEnvelopItemService")
public class RedEnvelopItemServiceImpl implements IRedEnvelopItemService {
    @Resource(name = "redEnvelopItemDao")
    private IRedEnvelopItemServiceDao redEnvelopItemServiceDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public RedEnvelopItem getById(String id) {
        return this.redEnvelopItemServiceDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param redEnvelopItem 需要删除的持久化对象
     */
    @Override
    public void delete(RedEnvelopItem redEnvelopItem) {
        this.redEnvelopItemServiceDao.deleteEntity(redEnvelopItem);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param redEnvelopItem 需要持久化的对象
     * @return
     */
    @Override
    public RedEnvelopItem add(RedEnvelopItem redEnvelopItem) {
        return this.redEnvelopItemServiceDao.saveEntity(redEnvelopItem);
    }

    /**
     * 更新一个持久化对象
     *
     * @param redEnvelopItem
     */
    @Override
    public void update(RedEnvelopItem redEnvelopItem) {
        this.update(redEnvelopItem);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<RedEnvelopItem> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.redEnvelopItemServiceDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<RedEnvelopItem> getPageData(Pager pager, IQueryParams queryParams) {
        return this.redEnvelopItemServiceDao.getPageData(pager, queryParams);
    }

    @Override
    public void addRedEnvelopItemByRedEvelop(RedEnvelop redEnvelop) {
        //float[] _moneies = RedEnvelopUtils.getItemAmouts(redEnvelop.getTotalMoney(), redEnvelop.getNumbers());
        List<Float> _moneies = RedEnvelopUtils.getRedMoneies(redEnvelop.getTotalMoney(), redEnvelop.getNumbers());
        for (int i = 0; i < redEnvelop.getNumbers(); i++) {
            RedEnvelopItem item = new RedEnvelopItem();
            item.setRedEnvelop(redEnvelop);
            item.setReceived(false);
            item.setAmountOfMoney(_moneies.get(i));
            this.redEnvelopItemServiceDao.saveEntity(item);
        }
    }

    @Override
    public void deleteByRedEnvelop(RedEnvelop redEnvelop) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("redEnvelop.envelopId", redEnvelop.getEnvelopId());
        this.redEnvelopItemServiceDao.executeDelete(queryParams);
    }
}
