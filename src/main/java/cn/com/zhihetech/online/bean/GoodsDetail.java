package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by YangDaiChun on 2015/11/13.
 */
@Entity
@Table(name = "t_goods_detail")
public class GoodsDetail extends SerializableAndCloneable {

    private String goodsDetailId;
    private Goods goods;
    private ImgInfo imgInfo;
    private int viewType;
    private String viewTarget;
    private int detailOrder;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "id", length = 36)
    public String getGoodsDetailId() {
        return goodsDetailId;
    }

    public void setGoodsDetailId(String goodsDetailId) {
        this.goodsDetailId = goodsDetailId;
    }

    @ManyToOne
    @JoinColumn(name = "goods_id")
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @ManyToOne
    @JoinColumn(name = "imginfo_id")
    public ImgInfo getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(ImgInfo imgInfo) {
        this.imgInfo = imgInfo;
    }

    @Column(name = "view_type")
    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Column(name = "view_target", length = 100)
    public String getViewTarget() {
        return viewTarget;
    }

    public void setViewTarget(String viewTarget) {
        this.viewTarget = viewTarget;
    }

    @Column(name = "detail_order")
    public int getDetailOrder() {
        return detailOrder;
    }

    public void setDetailOrder(int detailOrder) {
        this.detailOrder = detailOrder;
    }
}
