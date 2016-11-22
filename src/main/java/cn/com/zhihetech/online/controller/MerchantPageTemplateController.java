package cn.com.zhihetech.online.controller;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.MerchantPageTemplate;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.ResponseMessage;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.IMerchantPageTemplateService;
import cn.com.zhihetech.online.util.FreemarkerUtil;
import cn.com.zhihetech.online.util.PropertiesUtils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by YangDaiChun on 2016/6/28.
 */
@Controller
public class MerchantPageTemplateController extends SupportController {

    /*static String templateRealPath = "/template";
    static String merchantPageTemplate = "merchantPage.ftl";*/

    @Resource(name = "merchantPageTemplateService")
    private IMerchantPageTemplateService merchantPageTemplateService;

    /**
     * 商家ueditor编辑页面
     *
     * @return
     */
    @RequestMapping(value = "admin/merchantPageTemplate/addPage")
    public String addPage() {
        return "admin/pageTemplate/addMerchantPageTemplate";
    }

    public ResponseMessage freemarkerTest(String htmlPage, MerchantPageTemplate pageTemplate, HttpServletRequest request) throws IOException, TemplateException {
        String rootPath = request.getServletContext().getRealPath("/");
        String relativeFilePath = PropertiesUtils.getProperties().getProperty("MERCHANT_GENERATE_PAGE_PATH");
        String templateGeneratePath = rootPath + relativeFilePath + System.getProperty("file.separator");
        String fileName = System.currentTimeMillis() + ".html";
        Merchant merchant = new Merchant();
        merchant.setMerchantId(this.getCurrentMerchatId(request));
        pageTemplate.setMerchant(merchant);
        this.merchantPageTemplateService.executeAddTemplate(templateGeneratePath, fileName, htmlPage, pageTemplate);
        return executeResult();
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchantPageTemplate/add")
    public ResponseMessage addMerchantPageTemplateContent(MerchantPageTemplate merchantPageTemplate, HttpServletRequest request) {
        String pageContent = merchantPageTemplate.getTemplateContent();
        if (pageContent == null || StringUtils.isEmpty(pageContent.trim())) {
            throw new SystemException("你未添加任何内容！");
        }
        merchantPageTemplate.setMerchant(new Merchant(this.getCurrentMerchatId(request)));
        this.merchantPageTemplateService.add(merchantPageTemplate);
        return executeResult();
    }

    @RequestMapping(value = "admin/merchantPageTemplate")
    public String merchantPageListPage() {
        return "admin/pageTemplate/merchantPageTemplateList";
    }

    @ResponseBody
    @RequestMapping(value = "admin/api/merchantPageTemplate/list")
    public PageData<MerchantPageTemplate> getMerchantPageTemplateList(HttpServletRequest request) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("merchant.merchantId", this.getCurrentMerchatId(request));
        return this.merchantPageTemplateService.getPageData(this.createPager(request), queryParams);
    }

    @ResponseBody
    @RequestMapping(value = "admin/merchantPageTemplate/{id}")
    public ResponseMessage getMerchantPageTemplateById(@PathVariable(value = "id") String templateId) {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("templateId", templateId);
        List<Object> oContents = this.merchantPageTemplateService.getProperty("templateContent", queryParams, null);
        if (oContents == null || oContents.isEmpty()) {
            throw new SystemException("系统未找到对应的模板！");
        }
        String pageContent = (String) oContents.get(0);
        System.out.println(pageContent);
        return executeResult();
    }
}
