package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/12.
 */
@Entity
@Table(name = "t_focus_goods")
public class FocusGoods extends SerializableAndCloneable {

    private String focusGoodId;
    private User user;
    private Goods goods;
    private Date focusDate;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "focus_id", length = 36)
    public String getFocusGoodId() {
        return focusGoodId;
    }

    public void setFocusGoodId(String focusGoodId) {
        this.focusGoodId = focusGoodId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "goods_id",nullable = false)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }


    @Column(name = "focus_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFocusDate() {
        return focusDate;
    }

    public void setFocusDate(Date focusDate) {
        this.focusDate = focusDate;
    }
}
