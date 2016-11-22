package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.dao.IShoppingCartDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/1/12.
 */
@Repository("shoppingCartDao")
public class ShoppingCartDaoImpl extends SimpleSupportDao<ShoppingCart> implements IShoppingCartDao {
}
