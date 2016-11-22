package cn.com.zhihetech.util.hibernate.param.or;

import cn.com.zhihetech.util.hibernate.param.NullParam;

/**
 * Created by ShenYunjie on 2015/11/13.
 */
public class OrIsNullParam extends NullParam {

    public OrIsNullParam(String key) {
        super(key);
    }

    @Override
    public String getTargetHQL() {
        return LOGIC_OR_KEY + " " + this.key + " is null";
    }
}
