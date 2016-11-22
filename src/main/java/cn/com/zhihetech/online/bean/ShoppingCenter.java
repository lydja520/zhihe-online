package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by YangDaiChun on 2016/3/10.
 */
@Entity
@Table(name = "t_shopping_center")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShoppingCenter extends SerializableAndCloneable {

    private String scId;
    private String scName;
    private int scOrder;
    private boolean permit;
    private ImgInfo coverImg;

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "sc_id", length = 36, nullable = false)
    public String getScId() {
        return scId;
    }

    public void setScId(String scId) {
        this.scId = scId;
    }

    @Column(name = "sc_name", length = 100, nullable = false)
    public String getScName() {
        return scName;
    }

    public void setScName(String scName) {
        this.scName = scName;
    }

    @Column(name = "sc_order")
    public int getScOrder() {
        return scOrder;
    }

    public void setScOrder(int scOrder) {
        this.scOrder = scOrder;
    }

    @Column(name = "permit")
    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
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
