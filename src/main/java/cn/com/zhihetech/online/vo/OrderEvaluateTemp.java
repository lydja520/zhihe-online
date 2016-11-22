package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.GoodsScore;

import java.util.List;

/**
 * Created by YangDaiChun on 2016/2/18.
 */
public class OrderEvaluateTemp {
    private String orderId;
    private float score;
    private List<GoodsScore> goodsScores;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public List<GoodsScore> getGoodsScores() {
        return goodsScores;
    }

    public void setGoodsScores(List<GoodsScore> goodsScores) {
        this.goodsScores = goodsScores;
    }
}
