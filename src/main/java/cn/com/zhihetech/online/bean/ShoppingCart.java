package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

@Entity
@Table(name = "t_shopping_cart")
public class ShoppingCart extends SerializableAndCloneable {

    private String shoppingCartId;
    private User user;
    private Goods goods;
    private int amount;
    private Date focusDate;
    private Sku sku;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "shopping_cart_id", length = 36)
    public String getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(String shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Column(name = "amount")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Column(name = "focus_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFocusDate() {
        return focusDate;
    }

    public void setFocusDate(Date focusDate) {
        this.focusDate = focusDate;
    }

    @ManyToOne
    @JoinColumn(name = "sku_id")
    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

}
