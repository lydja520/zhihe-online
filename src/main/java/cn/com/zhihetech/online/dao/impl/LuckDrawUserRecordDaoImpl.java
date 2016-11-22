package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.LuckDrawUserRecord;
import cn.com.zhihetech.online.dao.ILuckDrawUserRecordDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ydc on 16-4-28.
 */
@Repository(value = "luckDrawUserRecordDao")
public class LuckDrawUserRecordDaoImpl extends SimpleSupportDao<LuckDrawUserRecord> implements ILuckDrawUserRecordDao{
}
