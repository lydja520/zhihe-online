package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;
import cn.com.zhihetech.online.dao.IReceivedGoodsAddressDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/1/25.
 */
@Repository(value = "receivedGoodsAddressDao")
public class ReceivedGoodsAddressDaoImpl extends SimpleSupportDao<ReceivedGoodsAddress> implements IReceivedGoodsAddressDao {
}
