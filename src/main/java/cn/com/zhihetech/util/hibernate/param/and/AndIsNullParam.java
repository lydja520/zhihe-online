package cn.com.zhihetech.util.hibernate.param.and;

import cn.com.zhihetech.util.hibernate.param.NullParam;

/**
 * Created by ShenYunjie on 2015/11/13.
 */
public class AndIsNullParam extends NullParam {

    public AndIsNullParam(String key) {
        super(key);
    }

    @Override
    public String getTargetHQL() {
        return LOGIC_AND_KEY + " " + this.key + " is null";
    }
}
