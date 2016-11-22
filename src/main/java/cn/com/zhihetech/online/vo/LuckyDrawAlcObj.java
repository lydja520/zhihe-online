package cn.com.zhihetech.online.vo;

import cn.com.zhihetech.online.bean.LuckyDraw;

import java.util.List;

/**
 * Created by ydc on 16-4-27.
 */
public class LuckyDrawAlcObj {

    public List<LuckyDraw> luckyDraws;  //抽奖奖项
    public long allLuckAmount;  //所有奖品总数量
    public float allLuckPercentage; //所有奖品所占的比例
    public long firstPrizeAmount; //比例最小的奖品数量
    public float firstPrizePercentage; //比例最小所占比例


    public List<LuckyDraw> getLuckyDraws() {
        return luckyDraws;
    }

    public void setLuckyDraws(List<LuckyDraw> luckyDraws) {
        this.luckyDraws = luckyDraws;
    }

    public long getAllLuckAmount() {
        return allLuckAmount;
    }

    public void setAllLuckAmount(long allLuckAmount) {
        this.allLuckAmount = allLuckAmount;
    }

    public float getAllLuckPercentage() {
        return allLuckPercentage;
    }

    public void setAllLuckPercentage(float allLuckPercentage) {
        this.allLuckPercentage = allLuckPercentage;
    }

    public long getFirstPrizeAmount() {
        return firstPrizeAmount;
    }

    public void setFirstPrizeAmount(long firstPrizeAmount) {
        this.firstPrizeAmount = firstPrizeAmount;
    }

    public float getFirstPrizePercentage() {
        return firstPrizePercentage;
    }

    public void setFirstPrizePercentage(float firstPrizePercentage) {
        this.firstPrizePercentage = firstPrizePercentage;
    }
}
