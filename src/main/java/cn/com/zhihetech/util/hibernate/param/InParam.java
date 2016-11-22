package cn.com.zhihetech.util.hibernate.param;

import cn.com.zhihetech.online.exception.SystemException;
import org.hibernate.Query;

import java.util.Collection;

/**
 * Created by ShenYunjie on 2015/12/10.
 */
public abstract class InParam extends SimpleParam {

    /**
     * @param key   属性名称
     * @param value 属性值
     */
    public InParam(String key, Object value) {
        super(key, value);
    }

    @Override
    public void initQueryValues(Query query) {
        if(!(this.value instanceof Collection)){
            throw new SystemException("IN查询参数错误");
        }
        query.setParameterList(getPlaceholderTitle(),(Collection) this.value);
    }
}
