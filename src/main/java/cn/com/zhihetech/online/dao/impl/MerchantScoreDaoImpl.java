package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.MerchantScore;
import cn.com.zhihetech.online.dao.IMerchantScoreDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/2/18.
 */
@Repository(value = "merchantScoreDao")
public class MerchantScoreDaoImpl extends SimpleSupportDao<MerchantScore> implements IMerchantScoreDao {
}
