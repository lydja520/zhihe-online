package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.MerchantPageTemplate;
import cn.com.zhihetech.online.dao.IMerchantPageTemplateDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by YangDaiChun on 2016/6/28.
 */
@Repository(value = "merchPageTemplateDao")
public class MerchantPageTemplateDaoImpl extends SimpleSupportDao<MerchantPageTemplate> implements IMerchantPageTemplateDao {
}
