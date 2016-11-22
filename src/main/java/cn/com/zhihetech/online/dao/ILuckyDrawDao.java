package cn.com.zhihetech.online.dao;

import cn.com.zhihetech.online.bean.LuckyDraw;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;

import java.util.List;

/**
 * Created by ydc on 16-4-22.
 */
public interface ILuckyDrawDao extends SupportDao<LuckyDraw>{

    List<LuckyDraw> getMinLuckDraw();
}
