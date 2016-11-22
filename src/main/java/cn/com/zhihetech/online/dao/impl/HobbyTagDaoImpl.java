package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.HobbyTag;
import cn.com.zhihetech.online.dao.IHobbyTagDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/1/13.
 */
@Repository("hobbyTagDao")
public class HobbyTagDaoImpl extends SimpleSupportDao<HobbyTag> implements IHobbyTagDao {
}
