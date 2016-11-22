package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.LoginJournal;
import cn.com.zhihetech.online.dao.ILoginJournalDao;
import cn.com.zhihetech.online.service.AbstractService;
import cn.com.zhihetech.online.service.ILoginJournalService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/4/19.
 */
@Service(value = "loginJournalService")
public class LoginJournalServiceImpl extends AbstractService<LoginJournal> implements ILoginJournalService {

    @Resource(name = "loginJournalDao")
    private ILoginJournalDao loginJournalDao;

    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public LoginJournal getById(String id) {
        return null;
    }

    /**
     * 删除持久化对象
     *
     * @param loginJournal 需要删除的持久化对象
     */
    @Override
    public void delete(LoginJournal loginJournal) {

    }

    /**
     * 添加一个对象到数据库
     *
     * @param loginJournal 需要持久化的对象
     * @return
     */
    @Override
    public LoginJournal add(LoginJournal loginJournal) {
        return null;
    }

    /**
     * 更新一个持久化对象
     *
     * @param loginJournal
     */
    @Override
    public void update(LoginJournal loginJournal) {

    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<LoginJournal> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<LoginJournal> getPageData(Pager pager, IQueryParams queryParams) {
        return  this.loginJournalDao.getPageData(pager, queryParams);
    }

    @Override
    public LoginJournal saveJournalAlways(LoginJournal journal) {
        if (journal.getFailReason() != null && journal.getFailReason().length() > 500) {
            journal.setFailReason(journal.getFailReason().substring(0, 499));
        }
        return this.loginJournalDao.saveEntity(journal);
    }

    //TODO:查询属性值集合
    /**
     *
     * @param selector
     * @param pager
     * @param queryParams
     * @return
     */
    @Override
    public List<Object> getProperty(String selector, Pager pager, IQueryParams queryParams) {
        return this.loginJournalDao.getProperty(selector,pager,queryParams);
    }

}
