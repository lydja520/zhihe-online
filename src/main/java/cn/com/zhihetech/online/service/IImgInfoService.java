package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface IImgInfoService extends SupportService<ImgInfo> {
    /**
     * 批量删除图片
     * @param imgIds 图片ID
     */
    void deleteBatch(String[] imgIds);
}
