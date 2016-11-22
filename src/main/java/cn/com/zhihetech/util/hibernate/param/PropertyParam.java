package cn.com.zhihetech.util.hibernate.param;

import org.hibernate.Query;

/**
 * Created by ShenYunjie on 2016/1/11.
 */
public abstract class PropertyParam implements IParam {

    private String value;
    private String key;
    private String keyAlias;

    public PropertyParam(String key, String value) {
        this.value = value;
        this.key = key;
        this.keyAlias = this.key;
    }

    /**
     * 自定属性key的别名，默认与key相同
     *
     * @param keyAlias 自定义"key"的别名
     */
    @Override
    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    /**
     * 获取参数在HQL语句中占位符标题。即："name =: userName"中的"userName"
     *
     * @return
     */
    @Override
    public String getPlaceholderTitle() {
        return this.keyAlias;
    }

    /**
     * 获取此参数值的最终HQL语句
     *
     * @return
     */
    @Override
    public abstract String getTargetHQL();

    /**
     * 初始化HQL语句中的占位符标题。
     *
     * @param query Hiberante查询对象
     * @return
     */
    @Override
    public final void initQueryValues(Query query) {
        return;
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
}
