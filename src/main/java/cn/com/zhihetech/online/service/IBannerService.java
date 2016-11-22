package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.util.hibernate.service.SupportService;

/**
 * Created by Administrator on 2015/12/16.
 */
public interface IBannerService extends SupportService<Banner> {

    /**
     * 删除轮播图
     * @param bannerId
     * @param imgInfoId
     */
     void deleteBannerAndImg(String bannerId,String imgInfoId);

    /**
     * 删除轮播图
     * @param bannerId
     */
    void delete(String bannerId);
}
