package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ShopShow;
import cn.com.zhihetech.online.dao.IShopShowDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/1/11.
 */
@Repository("shopShowDao")
public class ShopShowDaoImpl extends SimpleSupportDao<ShopShow> implements IShopShowDao {
}
