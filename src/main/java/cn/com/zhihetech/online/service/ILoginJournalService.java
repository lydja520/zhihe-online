package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.LoginJournal;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/19.
 */
public interface ILoginJournalService extends UpgradedService<LoginJournal> {

    /**
     * 不管是否登录成功都要保存登录日志
     *
     * @param journal
     * @return
     */
    LoginJournal saveJournalAlways(LoginJournal journal);

    List<Object> getProperty(String selector, Pager pager, IQueryParams queryParams);

}
