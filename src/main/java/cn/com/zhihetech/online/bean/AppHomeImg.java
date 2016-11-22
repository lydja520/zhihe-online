package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2016/3/21.
 */
@Entity
@Table(name = "t_app_home_img")
public class AppHomeImg extends SerializableAndCloneable{

    private String homeImgId;
    private ImgInfo imgInfo;
    private String homeImgName;

    @javax.persistence.Id
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "home_img_id",length = 36,nullable = false)
    public String getHomeImgId() {
        return homeImgId;
    }

    public void setHomeImgId(String homeImgId) {
        this.homeImgId = homeImgId;
    }

    @ManyToOne
    @JoinColumn(name = "img_id",nullable = false)
    public ImgInfo getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(ImgInfo imgInfo) {
        this.imgInfo = imgInfo;
    }

    @Column(name = "img_name",length = 100)
    public String getHomeImgName() {
        return homeImgName;
    }

    public void setHomeImgName(String homeImgName) {
        this.homeImgName = homeImgName;
    }
}
