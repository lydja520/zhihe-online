package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ydc on 16-8-15.
 */
@Entity
@Table(name = " t_rec_goods_for_admin")
public class RecommendGoodsForAdmin extends SerializableAndCloneable {

    private String recId;
    private String recName;
    private Goods goods;
    private ImgInfo coverImg;
    private int recOrder;
    private String desc;

    public RecommendGoodsForAdmin() {
    }

    public RecommendGoodsForAdmin(String recId) {
        this.recId = recId;
    }

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "rec_id", length = 36, nullable = false, unique = true, updatable = false)
    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    @Column(name = "rec_name", nullable = false, length = 100)
    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @ManyToOne
    @JoinColumn(name = "img_id", nullable = false)
    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }

    @Column(name = "rec_order", nullable = false)
    public int getRecOrder() {
        return recOrder;
    }

    public void setRecOrder(int recOrder) {
        this.recOrder = recOrder;
    }

    @Column(name = "rec_desc", nullable = true, length = 200)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
