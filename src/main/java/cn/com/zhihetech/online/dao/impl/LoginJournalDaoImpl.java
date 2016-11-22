package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.LoginJournal;
import cn.com.zhihetech.online.dao.ILoginJournalDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/4/19.
 */
@Repository("loginJournalDao")
public class LoginJournalDaoImpl extends SimpleSupportDao<LoginJournal> implements ILoginJournalDao {
}
