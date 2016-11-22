package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.LuckyDrawActivity;
import cn.com.zhihetech.online.dao.ILuckyDrawActivityDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ydc on 16-4-21.
 */
@Repository(value = "luckyDrawActivityDao")
public class LuckyDrawActivityDaoImpl extends SimpleSupportDao<LuckyDrawActivity> implements ILuckyDrawActivityDao{
}
