package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.PushBrithdate;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by YangDaiChun on 2016/3/24.
 */
public interface IPushBrithdateService extends SupportService<PushBrithdate> {

    void addPushBrithdates() throws ParseException;

    void executeUpdate(Map<String, Object> propertyAndValues, IQueryParams queryParams);

    void executePushUserbrithdayMsg(String pushBrithdateId, String userId, String pushInfo);
}
