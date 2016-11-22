package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.LuckyDraw;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;
import java.util.Map;

/**
 * Created by ydc on 16-4-22.
 */
public interface ILuckyDrawService extends SupportService<LuckyDraw> {

    void addNotWinLuckDraw(String lkDrawActId);

    double getCurrentPercentage(String selector,String property, Pager pager, IQueryParams queryParams);

    boolean getLkDrawActSubmitState(String lkDrawActId);

    void executeUpdate(Map<String,Object> paramAndValue,IQueryParams queryParams);
}
