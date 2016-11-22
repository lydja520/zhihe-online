package cn.com.zhihetech.util.hibernate.param.or;

import cn.com.zhihetech.util.hibernate.param.InParam;

/**
 * Created by ShenYunjie on 2015/12/10.
 */
public class OrNotInParam extends InParam {
    /**
     * @param key   属性名称
     * @param value 属性值
     */
    public OrNotInParam(String key, Object value) {
        super(key, value);
    }

    /**
     * 获取此参数值的最终HQL语句
     *
     * @return
     */
    @Override
    public String getTargetHQL() {
        return LOGIC_OR_KEY + " " + this.key + " not in :" + getPlaceholderTitle();
    }
}
