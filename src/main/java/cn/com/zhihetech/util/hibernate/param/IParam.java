package cn.com.zhihetech.util.hibernate.param;

import org.hibernate.Query;

import java.io.Serializable;

/**
 * hibernate参数接口
 * Created by ShenYunjie on 2015/11/12.
 */
public interface IParam extends Serializable, Cloneable {

    /**
     * HQL语句"and"关键词
     */
    final static String LOGIC_AND_KEY = "and";
    /**
     * HQL语句"or"关键词
     */
    final static String LOGIC_OR_KEY = "or";
    /**
     * 查询参数前缀
     */
    final static String PREFIX_KEY = "_query_";

    /**
     * 自定属性key的别名，默认与key相同
     *
     * @param keyAlias 自定义"key"的别名
     */
    void setKeyAlias(String keyAlias);

    /**
     * 获取参数在HQL语句中占位符标题。即："name =: userName"中的"userName"
     *
     * @return
     */
    String getPlaceholderTitle();

    /**
     * 获取此参数值的最终HQL语句
     *
     * @return
     */
    String getTargetHQL();

    /**
     * 初始化HQL语句中的占位符标题。
     *
     * @param query Hiberante查询对象
     * @return
     */
    void initQueryValues(Query query);

    /**
     * 获取查询参数的值
     *
     * @return
     */
    Object getValue();
}
