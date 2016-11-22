package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.FocusGoods;
import cn.com.zhihetech.online.dao.IFocusGoodsDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/1/8.
 */
@Repository(value = "focusGoodsDao")
public class FocusGoodsDaoImpl extends SimpleSupportDao<FocusGoods> implements IFocusGoodsDao {
}
