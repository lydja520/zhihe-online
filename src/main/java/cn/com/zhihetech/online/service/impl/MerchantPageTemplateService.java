package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.MerchantPageTemplate;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.dao.IMerchantPageTemplateDao;
import cn.com.zhihetech.online.service.IMerchantPageTemplateService;
import cn.com.zhihetech.online.util.CatchImgUtils;
import cn.com.zhihetech.online.util.FileWritterUtils;
import cn.com.zhihetech.online.util.PropertiesUtils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/6/28.
 */
@Service(value = "merchantPageTemplateService")
public class MerchantPageTemplateService implements IMerchantPageTemplateService {

    @Resource(name = "merchPageTemplateDao")
    private IMerchantPageTemplateDao merchantPageTemplateDao;


    @Override
    public MerchantPageTemplate getById(String id) {
        return null;
    }

    @Override
    public void delete(MerchantPageTemplate merchantPageTemplate) {

    }

    @Override
    public MerchantPageTemplate add(MerchantPageTemplate merchantPageTemplate) {
        return null;
    }

    @Override
    public void update(MerchantPageTemplate merchantPageTemplate) {

    }

    @Override
    public List<MerchantPageTemplate> getAllByParams(Pager pager, IQueryParams queryParams) {
        return null;
    }

    @Override
    public PageData<MerchantPageTemplate> getPageData(Pager pager, IQueryParams queryParams) {
        return this.merchantPageTemplateDao.getPageData(pager, queryParams);
    }


    @Override
    public void executeAddTemplate(String templateGeneratePath, String fileName, String htmlPage, MerchantPageTemplate pageTemplate) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("defaultState", true);
        long count = this.merchantPageTemplateDao.getRecordTotal(queryParams);
        if (count <= 0) {
            pageTemplate.setDefaultState(true);
        } else {
            pageTemplate.setDefaultState(false);
        }
        String coverImgSrc = CatchImgUtils.getFirstImageSrc(htmlPage);
        pageTemplate.setTemplateCoverImg(coverImgSrc);
        if (StringUtils.isEmpty(pageTemplate.getTemplateDesc())) {
            pageTemplate.setTemplateDesc(null);
        }
        String relativePath = PropertiesUtils.getProperties().getProperty("MERCHANT_GENERATE_PAGE_PATH");
        pageTemplate.setTemplateUrl(AppConfig.ServerConfig.SERVER_DOMAIN + "/" + relativePath + "/" + fileName);
        this.merchantPageTemplateDao.saveEntity(pageTemplate);
        FileWritterUtils.writerFileToLocalhost(templateGeneratePath, fileName, htmlPage);
    }

    @Override
    public List<Object> getProperty(String selector, IQueryParams queryParams, Pager pager) {
        return this.merchantPageTemplateDao.getProperty(selector, pager, queryParams);
    }
}
