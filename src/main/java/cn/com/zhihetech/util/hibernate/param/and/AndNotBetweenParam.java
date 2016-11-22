package cn.com.zhihetech.util.hibernate.param.and;

import cn.com.zhihetech.util.hibernate.param.BetweenParam;
import com.sun.istack.internal.NotNull;

/**
 * Created by ShenYunjie on 2015/11/13.
 */
public class AndNotBetweenParam<T> extends BetweenParam {


    public AndNotBetweenParam(@NotNull String key, @NotNull Object minValue, @NotNull Object maxValue) {
        super(key, minValue, maxValue);
    }

    /**
     * @return "and key not between (minValue and maxValue)"
     */
    @Override
    public String getTargetHQL() {
        return getNotBetweenHQL(LOGIC_AND_KEY, this.key);
    }
}