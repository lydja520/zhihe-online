package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.FocusGoods;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.dao.SupportDao;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by YangDaiChun on 2016/1/8.
 */
public interface IFocusGoodsService extends SupportService<FocusGoods> {

    long getFocusGoodsCount(IQueryParams queryParams);
}
