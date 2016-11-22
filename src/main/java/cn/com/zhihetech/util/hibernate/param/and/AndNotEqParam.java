package cn.com.zhihetech.util.hibernate.param.and;

/**
 * 且不等于参数
 * Created by ShenYunjie on 2015/11/13.
 */
public class AndNotEqParam extends AndEqualParam {
    /**
     * @param key   属性名称
     * @param value 属性值
     */
    public AndNotEqParam(String key, Object value) {
        super(key, value);
    }

    @Override
    public String getTargetHQL() {
        return LOGIC_AND_KEY + " " + this.key + " != :" + getPlaceholderTitle();
    }
}
