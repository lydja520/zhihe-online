package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Menu;
import cn.com.zhihetech.online.bean.Role;
import cn.com.zhihetech.online.dao.IRoleDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
@Repository("roleDao")
public class RoleDaoImpl extends SimpleSupportDao<Role> implements IRoleDao {
}
