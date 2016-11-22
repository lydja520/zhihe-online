package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.RecommendGoodsForAdmin;
import cn.com.zhihetech.online.dao.IRecGoodsForAdminDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ydc on 16-8-15.
 */
@Repository(value = "recGoodsForAdminDao")
public class RecGoodsForAdminDaoImpl extends SimpleSupportDao<RecommendGoodsForAdmin> implements IRecGoodsForAdminDao {
}
