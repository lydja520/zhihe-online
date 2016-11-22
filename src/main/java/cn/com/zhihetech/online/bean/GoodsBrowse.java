package cn.com.zhihetech.online.bean;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/13.
 */

@Entity
@Table(name = "t_goods_browse")     // 商品浏览记录表
public class GoodsBrowse extends SerializableAndCloneable {

    private String goodsBrowseId;
    private Goods goods;
    private Date browseDate;
    private User user;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "browse_id", length = 36)
    public String getGoodsBrowseId() {
        return goodsBrowseId;
    }

    public void setGoodsBrowseId(String goodsBrowseId) {
        this.goodsBrowseId = goodsBrowseId;
    }

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Column(name = "browse_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getBrowseDate() {
        return browseDate;
    }

    public void setBrowseDate(Date browseDate) {
        this.browseDate = browseDate;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
