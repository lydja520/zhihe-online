package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.MerchantBillErrRecord;
import cn.com.zhihetech.online.dao.IMerchantBillErrRecordDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/5/17.
 */
@Repository(value = "merchantBilErrRecordDao")
public class MerchantBillErrRecordDaoImpl extends SimpleSupportDao<MerchantBillErrRecord> implements IMerchantBillErrRecordDao {
}
