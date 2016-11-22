package cn.com.zhihetech.online.service;

import cn.com.zhihetech.online.bean.MerchantPageTemplate;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.io.IOException;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/6/28.
 */
public interface IMerchantPageTemplateService extends SupportService<MerchantPageTemplate> {

    void executeAddTemplate(String templateGeneratePath, String fileName, String htmlPage, MerchantPageTemplate pageTemplate) throws IOException;

    List<Object> getProperty(String selector, IQueryParams queryParams, Pager pager);
}
