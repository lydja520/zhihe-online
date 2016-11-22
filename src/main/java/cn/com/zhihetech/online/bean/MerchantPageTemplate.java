package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2016/6/28.
 */
@Entity
@Table(name = "t_merch_page_template")
public class MerchantPageTemplate extends SerializableAndCloneable {

    private String templateId;
    private Merchant merchant;  //页面对应的商家
    private String templateName;   //页面模板名字
    private String templateCoverImg; //页面模板的封面图
    private String templateDesc;  //页面模板的描述
    private String templateUrl; //页面模板的地址
    private String templateContent;  //页面模板内容
    private boolean defaultState;  //是否是默认模板

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "template_id", length = 36, nullable = false)
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Column(name = "template_name", length = 100, nullable = false)
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Column(name = "template_coverImg_url")
    public String getTemplateCoverImg() {
        return templateCoverImg;
    }

    public void setTemplateCoverImg(String templateCoverImg) {
        this.templateCoverImg = templateCoverImg;
    }

    @Column(name = "template_desc", nullable = true)
    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    @Column(name = "template_url", nullable = false)
    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "template_content", columnDefinition = "TEXT", nullable = false)
    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    @Column(name = "default_state")
    public boolean isDefaultState() {
        return defaultState;
    }

    public void setDefaultState(boolean defaultState) {
        this.defaultState = defaultState;
    }
}
