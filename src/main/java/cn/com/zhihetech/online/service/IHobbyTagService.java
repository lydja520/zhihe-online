package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.HobbyTag;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/1/13.
 */
public interface IHobbyTagService extends SupportService<HobbyTag> {
    List<HobbyTag> getTagsByParentId(String parentId);
}
