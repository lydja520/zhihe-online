package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/13.
 */

@Entity
@Table(name = "t_banner")
public class Banner extends SerializableAndCloneable {

    private String bannerId;
    private ImgInfo imgInfo;   //轮播图图片
    private int viewType;      //跳转的页面类型
    private String viewTargert;  //跳转到哪里
    private String viewTargetTitle; //跳转目标标题
    private int bannerType = Constant.BANNER_ONE;   //轮播图所处的位置
    private int bannerOrder;      //轮播图顺序
    private Date createDate;

    private String displayLocation; //轮播图所在界面
    private String displayTarget;   //轮播图跳转目标地址

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "banner_id", length = 36)
    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    @ManyToOne
    @JoinColumn(name = "imginfo_id")
    public ImgInfo getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(ImgInfo imgInfo) {
        this.imgInfo = imgInfo;
    }

    @Column(name = "view_type", nullable = false)
    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Column(name = "view_target", length = 100)
    public String getViewTargert() {
        return viewTargert;
    }

    public void setViewTargert(String viewTargert) {
        this.viewTargert = viewTargert;
    }

    @Column(name = "target_title", length = 50)
    public String getViewTargetTitle() {
        return viewTargetTitle;
    }

    public void setViewTargetTitle(String viewTargetTitle) {
        this.viewTargetTitle = viewTargetTitle;
    }

    @Column(name = "banner_type")
    public int getBannerType() {
        return bannerType;
    }

    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
    }

    @Column(name = "banner_order", nullable = false)
    public int getBannerOrder() {
        return bannerOrder;
    }


    public void setBannerOrder(int bannerOrder) {
        this.bannerOrder = bannerOrder;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /*====================数据库无关属性======================*/
    @Transient
    public String getDisplayLocation() {
        switch (bannerType) {
            case Constant.BANNER_ONE:
                return "主页";
            case Constant.BANNER_TWO:
                return "购物中心";
            case Constant.BANNER_THREE:
                return "特色街区";
            case Constant.BANNER_FOUR:
                return "优'+'店";
            default:
                return "无";
        }
    }

    public void setDisplayLocation(String displayLocation) {
        this.displayLocation = displayLocation;
    }

    @Transient
    public String getDisplayTarget() {
        switch (viewType) {
            case Constant.BANNER_VIEWTYPE_MERCHANT:
                return MessageFormat.format("商家({0})", this.getViewTargetTitle());
            case Constant.BANNER_VIEWTYPE_GOODS:
                return MessageFormat.format("商品({0})", this.getViewTargetTitle());
            case Constant.BANNER_VIEWTYPE_ACTIVITY:
                return MessageFormat.format("活动({0})", this.getViewTargetTitle());
            case Constant.BANNER_VIEWTYPE_PAGE:
                return MessageFormat.format("地址({0})", this.getViewTargetTitle());
            default:
                return "无跳转";
        }
    }

    public void setDisplayTarget(String displayTarget) {
        this.displayTarget = displayTarget;
    }
}
