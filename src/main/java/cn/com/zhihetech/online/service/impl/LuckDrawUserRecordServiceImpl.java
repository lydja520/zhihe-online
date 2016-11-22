package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.LuckDrawUserRecord;
import cn.com.zhihetech.online.service.ILuckDrawUserRecordService;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ydc on 16-4-28.
 */
@Service(value = "luckDrawUserRecordService")
public class LuckDrawUserRecordServiceImpl  implements ILuckDrawUserRecordService{

    @Override
    public LuckDrawUserRecord getById(String id) {
        return null;
    }

    @Override
    public void delete(LuckDrawUserRecord luckDrawUserRecord) {

    }

    @Override
    public LuckDrawUserRecord add(LuckDrawUserRecord luckDrawUserRecord) {
        return null;
    }

    @Override
    public void update(LuckDrawUserRecord luckDrawUserRecord) {

    }

    @Override
    public List<LuckDrawUserRecord> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<LuckDrawUserRecord> getPageData(Pager pager, IQueryParams queryParams) {
        return null;
    }
}
