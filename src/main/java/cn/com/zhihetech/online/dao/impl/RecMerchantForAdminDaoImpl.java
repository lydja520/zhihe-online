package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.RecommendMerchantForAdmin;
import cn.com.zhihetech.online.dao.IRecMerchantForAdminDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ydc on 16-8-15.
 */
@Repository(value = "recMerchantForAdminDao")
public class RecMerchantForAdminDaoImpl extends SimpleSupportDao<RecommendMerchantForAdmin> implements IRecMerchantForAdminDao {
}
