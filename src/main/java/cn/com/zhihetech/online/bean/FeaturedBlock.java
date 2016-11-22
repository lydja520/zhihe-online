package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by YangDaiChun on 2016/3/10.
 */
@Entity
@Table(name = "t_featured_block")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FeaturedBlock extends SerializableAndCloneable {

    private String fbId;   //Id
    private String fbName;  //特色街区名
    private int fbOrder;  //顺序
    private boolean permit;   //是否启用
    private Area area;  //所属区域
    private ImgInfo coverImg;  //封面图

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "fb_id", length = 36, nullable = false)
    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    @Column(name = "fb_name", length = 100, nullable = false)
    public String getFbName() {
        return fbName;
    }

    public void setFbName(String fbName) {
        this.fbName = fbName;
    }

    @Column(name = "fb_order")
    public int getFbOrder() {
        return fbOrder;
    }

    public void setFbOrder(int fbOrder) {
        this.fbOrder = fbOrder;
    }

    @Column(name = "permit")
    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = true)
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @ManyToOne
    @JoinColumn(name = "imgInfo_id")
    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }
}
