package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ScroNewsOnApp;
import cn.com.zhihetech.online.dao.IScroNewsOnAppDao;
import cn.com.zhihetech.online.service.IScroNewOnAppService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/8/19.
 */
@Service("scroNewOnAppService")
public class ScroNewOnAppServiceImpl implements IScroNewOnAppService {

    @Resource(name = "scroNewOnAppDao")
    private IScroNewsOnAppDao scroNewsOnAppDao;

    @Override
    public ScroNewsOnApp getById(String id) {
        return this.scroNewsOnAppDao.findEntityById(id);
    }

    @Override
    public void delete(ScroNewsOnApp scroNewsOnApp) {
        this.scroNewsOnAppDao.deleteEntity(scroNewsOnApp);
    }

    @Override
    public ScroNewsOnApp add(ScroNewsOnApp scroNewsOnApp) {
        return this.scroNewsOnAppDao.saveEntity(scroNewsOnApp);
    }

    @Override
    public void update(ScroNewsOnApp scroNewsOnApp) {
        this.scroNewsOnAppDao.updateEntity(scroNewsOnApp);
    }

    @Override
    public List<ScroNewsOnApp> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.scroNewsOnAppDao.getEntities(pager, queryParams);
    }

    @Override
    public PageData<ScroNewsOnApp> getPageData(Pager pager, IQueryParams queryParams) {
        PageData<ScroNewsOnApp> scroNewsOnAppPageData = this.scroNewsOnAppDao.getPageData(pager, queryParams);
/*        for (ScroNewsOnApp scroNewsOnApp : scroNewsOnAppPageData.getRows()) {
            switch (scroNewsOnApp.getJumpType()) {
                case goods:
                    queryParams = new GeneralQueryParams();
                    queryParams.andEqual("goodsId", scroNewsOnApp.getJumpTarget());
                    List<Object> goodsNames =

            }
        }*/
        return scroNewsOnAppPageData;
    }
}
