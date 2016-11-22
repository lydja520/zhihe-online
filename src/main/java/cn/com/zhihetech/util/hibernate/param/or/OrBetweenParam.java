package cn.com.zhihetech.util.hibernate.param.or;

import cn.com.zhihetech.util.hibernate.param.BetweenParam;
import com.sun.istack.internal.NotNull;

/**
 * Created by ShenYunjie on 2015/11/13.
 */
public class OrBetweenParam<T> extends BetweenParam {

    /**
     * 对应HQL中的between and
     *
     * @param key      属性名称
     * @param minValue between and语句中的起始值
     * @param maxValue between and语句中的终止值
     */
    public OrBetweenParam(@NotNull String key, @NotNull T minValue, @NotNull T maxValue) {
        super(key, minValue, maxValue);
    }

    @Override
    public String getTargetHQL() {
        return getBetweenHQL(LOGIC_OR_KEY,this.key);
    }
}
