package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YangDaiChun on 2016/5/12.
 */
@Entity
@Table(name = "t_order_edit_record")
public class OrderEditRecord extends SerializableAndCloneable {

    private String orderEditRecordId;
    private Order order;
    private float originalPrice;  //原来的价格
    private float nowPrice;  //修改后的价格
    private Date editDate;  //修改时间
    private int orderState;  //修改时订单原来的状态

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "order_ed_rd_id", length = 36, nullable = false)
    public String getOrderEditRecordId() {
        return orderEditRecordId;
    }

    public void setOrderEditRecordId(String orderEditRecordId) {
        this.orderEditRecordId = orderEditRecordId;
    }

    @ManyToOne
    @JoinColumn(name = "order_id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Column(name = "original_price")
    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    @Column(name = "now_price")
    public float getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(float nowPrice) {
        this.nowPrice = nowPrice;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "edit_date")
    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    @Column(name = "order_state")
    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }
}
