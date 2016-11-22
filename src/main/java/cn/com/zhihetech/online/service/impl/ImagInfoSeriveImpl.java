package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.dao.IImgInfoDao;
import cn.com.zhihetech.online.service.IImgInfoService;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2015/11/26.
 */
@Service("imgInfoService")
public class ImagInfoSeriveImpl implements IImgInfoService {
    @Resource(name = "imgInfoDao")
    private IImgInfoDao imgInfoDao;

    @Override
    public ImgInfo getById(String id) {
        return this.imgInfoDao.findEntityById(id);
    }

    @Override
    public void delete(ImgInfo imgInfo) {
        this.imgInfoDao.deleteEntity(imgInfo);
    }

    @Override
    public ImgInfo add(ImgInfo imgInfo) {
        return imgInfoDao.saveEntity(imgInfo);
    }

    @Override
    public void update(ImgInfo imgInfo) {

    }

    @Override
    public List<ImgInfo> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<ImgInfo> getPageData(Pager pager, IQueryParams queryParams) {
        return this.imgInfoDao.getPageData(pager, queryParams);
    }


    @Override
    public void deleteBatch(String[] imgIds) {
        if (imgIds == null) {
            return;
        }
        for (String imgId : imgIds) {
            IQueryParams queryParams = new GeneralQueryParams();
            queryParams.andEqual("imgInfoId", imgId);
            this.imgInfoDao.executeDelete(queryParams);
        }
    }
}
