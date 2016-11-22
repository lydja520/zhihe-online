package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.LuckyDrawDetail;
import cn.com.zhihetech.online.dao.ILuckyDrawDetailDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ydc on 16-4-28.
 */
@Repository(value = "luckDrawDetailDao")
public class LuckyDrawDetailDaoImpl extends SimpleSupportDao<LuckyDrawDetail> implements ILuckyDrawDetailDao{
}
