package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2015/11/12.
 */
@Entity
@Table(name = "t_img_info")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ImgInfo extends SerializableAndCloneable {

    private String imgInfoId;
    private int width;
    private int height;
    private String key;
    private Date createDate;
    private String bucket;
    private String owner;   //图片拥有者，如果是普通用户则对应普通用户的ID,如果是商家用户则为商家ID，否则为空

    private String domain = AppConfig.QiNiuConfig.DOMIN;

    private String url;

    public ImgInfo() {
    }

    public ImgInfo(String imgInfoId) {
        super();
        this.imgInfoId = imgInfoId;
    }

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "img_id", length = 36)
    public String getImgInfoId() {
        return imgInfoId;
    }

    public void setImgInfoId(String imgInfoId) {
        this.imgInfoId = imgInfoId;
    }

    @Column(name = "img_width")
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Column(name = "img_height")
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Column(name = "img_key", length = 50, nullable = false)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "img_bucket", length = 50, nullable = false)
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Column(name = "owner", length = 50, nullable = true)
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Column(name = "img_domain", length = 50, nullable = false)
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Transient
    public String getUrl() {
        setUrl(this.domain + this.key);
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
