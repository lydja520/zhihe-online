package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.MerchantAdv;
import cn.com.zhihetech.online.dao.IMerchantAdvDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/4/22.
 */
@Repository("merchantAdvDao")
public class MerchantAdvDaoImpl extends SimpleSupportDao<MerchantAdv> implements IMerchantAdvDao {
}
