package cn.com.zhihetech.util.hibernate.param.and;

import cn.com.zhihetech.util.hibernate.param.PropertyParam;

/**
 * Created by ShenYunjie on 2016/1/11.
 */
public class AndPropertyParam extends PropertyParam {

    public AndPropertyParam(String key, String value) {
        super(key, value);
    }

    /**
     * 获取此参数值的最终HQL语句
     *
     * @return
     */
    @Override
    public String getTargetHQL() {
        return LOGIC_AND_KEY + " " + getValue();
    }
}
