package cn.com.zhihetech.util.hibernate.param.and;

import cn.com.zhihetech.util.hibernate.param.InParam;

/**
 * Created by ShenYunjie on 2015/12/10.
 */
public class AndNotInParam extends InParam {
    /**
     * @param key   属性名称
     * @param value 属性值
     */
    public AndNotInParam(String key, Object value) {
        super(key, value);
    }

    /**
     * 获取此参数值的最终HQL语句
     *
     * @return
     */
    @Override
    public String getTargetHQL() {
        return LOGIC_AND_KEY + " " + this.key + " not in :" + getPlaceholderTitle();
    }
}
