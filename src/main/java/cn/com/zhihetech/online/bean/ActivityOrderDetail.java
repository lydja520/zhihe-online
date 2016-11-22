package cn.com.zhihetech.online.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ydc on 16-5-27.
 */
@Entity
@Table(name = "t_acticity_order_detail")
public class ActivityOrderDetail {

    private String activityOrderDetailId;
    private ActivityOrder activityOrder;
    private OrderType orderType;  //订单类型，是红包，还是参加活动的费用
    private float money;  //金额
    private boolean payState; //是否已经支付
    private String orderTypeId; //orderType是redEvelop则，此处是红包id
    private String merchantId;  //对应的商家id
    private String activityId;//对应的互动id

    public enum OrderType {
        redEvelop, activityCost
    }

    @javax.persistence.Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "activity_order_detail_id", length = 36, nullable = false)
    public String getActivityOrderDetailId() {
        return activityOrderDetailId;
    }

    public void setActivityOrderDetailId(String activityOrderDetailId) {
        this.activityOrderDetailId = activityOrderDetailId;
    }

    @ManyToOne
    @JoinColumn(name = "activity_order_id", nullable = true)
    public ActivityOrder getActivityOrder() {
        return activityOrder;
    }

    public void setActivityOrder(ActivityOrder activityOrder) {
        this.activityOrder = activityOrder;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Column(name = "money", nullable = false)
    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    @Column(name = "pay_state", nullable = false)
    public boolean isPayState() {
        return payState;
    }

    public void setPayState(boolean payState) {
        this.payState = payState;
    }

    @Column(name = "order_type_id", nullable = false)
    public String getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(String orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    @Column(name = "merchant_id", length = 36, nullable = false)
    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @Column(name = "activity_id", length = 36, nullable = false)
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
