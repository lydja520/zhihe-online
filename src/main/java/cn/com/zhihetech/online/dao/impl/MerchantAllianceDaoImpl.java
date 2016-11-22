package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.MerchantAlliance;
import cn.com.zhihetech.online.dao.IMerchantAllianceDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2015/12/8.
 */
@Repository("merchantAllianceDao")
public class MerchantAllianceDaoImpl extends SimpleSupportDao<MerchantAlliance> implements IMerchantAllianceDao {
}
