package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.MerchantBrowse;
import cn.com.zhihetech.online.dao.IMerchantBrowseDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/4/18.
 */
@Repository("merchantBrowseDao")
public class MerchantBrowseDaoImpl extends SimpleSupportDao<MerchantBrowse> implements IMerchantBrowseDao {
}
