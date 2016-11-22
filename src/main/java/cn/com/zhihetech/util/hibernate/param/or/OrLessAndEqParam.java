package cn.com.zhihetech.util.hibernate.param.or;


import cn.com.zhihetech.util.hibernate.param.SimpleParam;

/**
 * 或小于等于参数类
 * Created by ShenYunjie on 2015/11/13.
 */
public class OrLessAndEqParam extends SimpleParam {

    /**
     * @param key   属性名称
     * @param value 属性值
     */
    public OrLessAndEqParam(String key, Object value) {
        super(key, value);
    }

    @Override
    public String getTargetHQL() {
        return LOGIC_OR_KEY + " " + this.key + " <= :" + getPlaceholderTitle();
    }
}
