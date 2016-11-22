package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.FeaturedBlock;
import cn.com.zhihetech.util.hibernate.service.SupportService;
import org.springframework.stereotype.Service;

/**
 * Created by YangDaiChun on 2016/3/10.
 */
public interface IFeaturedBlockService extends SupportService<FeaturedBlock>{

    void updateFbDelState(String fbId,boolean permit);
}
