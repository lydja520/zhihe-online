package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ShoppingCenter;
import cn.com.zhihetech.online.dao.IShoppingCenterDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/3/10.
 */
@Repository(value = "shoppingCenterDao")
public class ShoppingCenterDaoImpl extends SimpleSupportDao<ShoppingCenter> implements IShoppingCenterDao{
}