package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2016/2/18.
 */
@Entity
@Table(name = "t_goods_score")
public class GoodsScore extends SerializableAndCloneable{
    private String goodsScoreId;
    private float score;
    private String evaluate;
    private Goods goods;
    private String goodsId;
    private Date createDate;
    private User user;
    private Order order;

    @Id
    @GenericGenerator(name = "systemUUID",strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "id",length = 36,nullable = false)
    public String getGoodsScoreId() {
        return goodsScoreId;
    }

    public void setGoodsScoreId(String goodsScoreId) {
        this.goodsScoreId = goodsScoreId;
    }

    @Column(name = "score")
    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Column(name = "evaluate")
    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    @ManyToOne
    @JoinColumn(name = "goods_id")
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Transient
    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "order_id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
