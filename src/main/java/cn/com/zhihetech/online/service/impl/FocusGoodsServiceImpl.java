package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.FocusGoods;
import cn.com.zhihetech.online.dao.IFocusGoodsDao;
import cn.com.zhihetech.online.service.IFocusGoodsService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/1/8.
 */
@Service(value = "focusGoodsService")
public class FocusGoodsServiceImpl implements IFocusGoodsService {

    @Resource(name = "focusGoodsDao")
    private IFocusGoodsDao focusGoodsDao;

    @Override
    public FocusGoods getById(String id) {
        return this.focusGoodsDao.findEntityById(id);
    }

    @Override
    public void delete(FocusGoods focusGoods) {
            this.focusGoodsDao.deleteEntity(focusGoods);
    }

    @Override
    public FocusGoods add(FocusGoods focusGoods) {
        return this.focusGoodsDao.saveEntity(focusGoods);
    }

    @Override
    public void update(FocusGoods focusGoods) {

    }

    @Override
    public List<FocusGoods> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.focusGoodsDao.getEntities(pager,queryParams);
    }

    @Override
    public PageData<FocusGoods> getPageData(Pager pager, IQueryParams queryParams) {
        return this.focusGoodsDao.getPageData(pager,queryParams);
    }

    @Override
    public long getFocusGoodsCount(IQueryParams queryParams) {
        return this.focusGoodsDao.getRecordTotal(queryParams);
    }
}
