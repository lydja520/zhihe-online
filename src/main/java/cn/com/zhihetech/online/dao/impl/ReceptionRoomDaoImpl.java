package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ReceptionRoom;
import cn.com.zhihetech.online.dao.IReceptionRoomDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/4/5.
 */
@Repository("receptionRoomDao")
public class ReceptionRoomDaoImpl extends SimpleSupportDao<ReceptionRoom> implements IReceptionRoomDao {
}
