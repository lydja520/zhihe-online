package cn.com.zhihetech.util.hibernate.param;

import org.hibernate.Query;

/**
 * Created by ShenYunjie on 2015/11/13.
 *
 * @version 1.0
 */
public abstract class SimpleParam implements IParam {

    protected String key;
    protected Object value;
    private String keyAlias;

    /**
     * @param key   属性名称
     * @param value 属性值
     */
    public SimpleParam(String key, Object value) {
        this.key = key;
        this.value = value;
        this.keyAlias = key;
    }

    /**
     * 获取查询参数的值
     *
     * @return
     */
    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    @Override
    public String getPlaceholderTitle() {
        return PREFIX_KEY + this.keyAlias.replaceAll("\\.", "_");
    }

    @Override
    public void initQueryValues(Query query) {
        query.setParameter(getPlaceholderTitle(),this.value);
    }
}
