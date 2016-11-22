package cn.com.zhihetech.util.hibernate.param.or;

import cn.com.zhihetech.util.hibernate.param.BetweenParam;
import com.sun.istack.internal.NotNull;

/**
 * Created by ShenYunjie on 2015/11/13.
 */
public class OrNotBetweenParam<T> extends BetweenParam {


    public OrNotBetweenParam(@NotNull String key, @NotNull Object minValue, @NotNull Object maxValue) {
        super(key, minValue, maxValue);
    }

    /**
     * @return "or key not between (minValue and maxValue)"
     */
    @Override
    public String getTargetHQL() {
        return getNotBetweenHQL(LOGIC_OR_KEY,this.key);
    }
}
