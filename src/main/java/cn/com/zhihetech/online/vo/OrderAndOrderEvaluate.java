package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.GoodsScore;
import cn.com.zhihetech.online.bean.Order;

import java.util.List;

/**
 * Created by YangDaiChun on 2016/4/5.
 */
public class OrderAndOrderEvaluate {

    private Order order;
    private List<GoodsScore> goodsScores;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<GoodsScore> getGoodsScores() {
        return goodsScores;
    }

    public void setGoodsScores(List<GoodsScore> goodsScores) {
        this.goodsScores = goodsScores;
    }
}
