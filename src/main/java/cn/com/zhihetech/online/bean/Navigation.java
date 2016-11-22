package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

@Entity
@Table(name = "t_navigation")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Navigation extends SerializableAndCloneable {
    private String navigationId;
    private String navigationName;  //导航名
    private String viewTargert;   //跳转到的模块
    private int order;   //导航顺序
    private boolean permit;   //是否启用
    private String desc;    //描述
    private ImgInfo img;   //导航图标
    private String viewUrl; //导航跳转的url地址，viewTarget为10时才有效

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "nav_id", length = 36)
    public String getNavigationId() {
        return navigationId;
    }

    public void setNavigationId(String navigationId) {
        this.navigationId = navigationId;
    }

    @Column(name = "nava_name", length = 50, nullable = false)
    public String getNavigationName() {
        return navigationName;
    }

    public void setNavigationName(String navigationName) {
        this.navigationName = navigationName;
    }

    @ManyToOne
    @JoinColumn(name = "nav_img")
    public ImgInfo getImg() {
        return this.img;
    }

    public void setImg(ImgInfo img) {
        this.img = img;
    }

    @Column(name = "nav_target", length = 400)
    public String getViewTargert() {
        return viewTargert;
    }

    public void setViewTargert(String viewTargert) {
        this.viewTargert = viewTargert;
    }

    @Column(name = "nav_order")
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Column(name = "nav_permit")
    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    @Column(name = "nav_desc", length = 300)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 点击导航跳转的页面地址（只有当viewTargert为10时才有效）
     *
     * @return
     */
    @Column(name = "view_url", length = 100)
    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }
}
