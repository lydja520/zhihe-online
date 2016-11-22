package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.dao.IImgInfoDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2015/11/19.
 */
@Repository("imgInfoDao")
public class ImgInfoDaoImpl extends SimpleSupportDao<ImgInfo> implements IImgInfoDao {
}
