package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Menu;
import cn.com.zhihetech.online.dao.IMenuDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;


/**
 * Created by ShenYunjie on 2015/11/17.
 */
@Repository("menuDao")
public class MenuDaoImpl extends SimpleSupportDao<Menu> implements IMenuDao {
}
