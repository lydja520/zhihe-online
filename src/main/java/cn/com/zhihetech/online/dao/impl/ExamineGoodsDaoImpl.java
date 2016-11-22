package cn.com.zhihetech.online.dao.impl;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.dao.IExamineGoodsDao;
import cn.com.zhihetech.util.hibernate.dao.SimpleSupportDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ShenYunjie on 2016/7/12.
 */
@Repository("examineGoodsDao")
public class ExamineGoodsDaoImpl extends SimpleSupportDao<Goods> implements IExamineGoodsDao {
}
