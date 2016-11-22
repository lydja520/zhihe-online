package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.bean.OrderDetail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/7/14.
 */
public class OrderAndOrderDetailInfo implements Serializable, Cloneable {
    private Order order;
    private List<OrderDetail> orderDetails;

    public OrderAndOrderDetailInfo() {
    }

    public OrderAndOrderDetailInfo(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
