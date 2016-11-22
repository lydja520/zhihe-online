package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Wallet;
import cn.com.zhihetech.online.dao.IWalletDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/3/4.
 */
@Repository(value = "walletDao")
public class WalletDaoImpl extends SimpleSupportDao<Wallet> implements IWalletDao{
}
