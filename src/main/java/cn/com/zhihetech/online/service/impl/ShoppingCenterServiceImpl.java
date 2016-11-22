package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ShoppingCenter;
import cn.com.zhihetech.online.dao.IShoppingCenterDao;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IShopShowService;
import cn.com.zhihetech.online.service.IShoppingCenterService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/3/10.
 */
@Service(value = "shoppingCenterService")
public class ShoppingCenterServiceImpl implements IShoppingCenterService {

    @Resource(name = "shoppingCenterDao")
    private IShoppingCenterDao shoppingCenterDao;

    @Override
    public ShoppingCenter getById(String id) {
        return null;
    }

    @Override
    public void delete(ShoppingCenter shoppingCenter) {

    }

    @Override
    public ShoppingCenter add(ShoppingCenter shoppingCenter) {
        return this.shoppingCenterDao.saveEntity(shoppingCenter);
    }

    @Override
    public void update(ShoppingCenter shoppingCenter) {
        this.shoppingCenterDao.updateEntity(shoppingCenter);
    }

    @Override
    public List<ShoppingCenter> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.shoppingCenterDao.getEntities(pager,queryParams);
    }

    @Override
    public PageData<ShoppingCenter> getPageData(Pager pager, IQueryParams queryParams) {
        return this.shoppingCenterDao.getPageData(pager, queryParams);
    }

    @Override
    public void updateDeleteState(String scId, boolean permit) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("scId", scId);
        List<Object> permits = this.shoppingCenterDao.getProperty("permit", null, queryParams);
        Map<String, Object> paramAndValue = new HashMap<>();
        if (permits.size() <= 0) {
            throw new SystemException("不存在该条记录！");
        } else {
            boolean _permit = (boolean) permits.get(0);
            if (_permit && !permit) {
                paramAndValue.put("permit", false);
            } else if (!_permit && permit) {
                paramAndValue.put("permit",true);
            } else {
                throw new SystemException("请勿重复操作");
            }
            this.shoppingCenterDao.executeUpdate(paramAndValue, queryParams);
        }
    }
}
