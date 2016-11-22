package cn.com.zhihetech.util.hibernate.param;

import org.hibernate.Query;

/**
 * Created by ShenYunjie on 2015/11/13.
 */
public abstract class NullParam implements IParam {

    protected String key;
    private String keyAlias;

    public NullParam(String key) {
        this.key = key;
        this.keyAlias = key;
    }

    /**
     * 获取查询参数的值
     *
     * @return
     */
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void initQueryValues(Query query) {
        return;
    }

    @Override
    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    @Override
    public String getPlaceholderTitle() {
        return PREFIX_KEY + this.keyAlias.replaceAll("\\.", "_");
    }
}
