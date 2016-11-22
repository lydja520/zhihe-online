package cn.com.zhihetech.util.hibernate.param;

import com.sun.istack.internal.NotNull;

import org.hibernate.Query;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/11/13.
 */
public abstract class BetweenParam implements IParam {

    protected final int MIN_FLAG = 1;
    protected final int MAX_FLAG = 2;

    protected final String MIN_KEY = "_min_key_";
    protected final String MAX_KEY = "_max_key_";

    protected String key;
    protected Map<String, Object> minAndMax = new HashMap<>();
    protected String keyAlias;

    /**
     * 对应HQL中的between and
     * @param key   属性名称
     * @param minValue  between and语句中的起始值
     * @param maxValue between and语句中的终止值
     */
    public BetweenParam(@NotNull String key, @NotNull Object minValue, @NotNull Object maxValue) {
        this.key = key;
        minAndMax.put(MIN_KEY, minValue);
        minAndMax.put(MAX_KEY, maxValue);
        this.keyAlias = key;
    }

    @Override
    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    /**
     * 获取查询参数的值
     *
     * @return
     */
    @Override
    public Object getValue() {
        return this.minAndMax;
    }

    @Override
    public String getPlaceholderTitle() {
        return PREFIX_KEY + this.keyAlias.replaceAll("\\.", "_");
    }

    protected String getMinOrMaxPlaceholder(int flag){
        if(flag == MIN_FLAG){
            return getPlaceholderTitle() + MIN_KEY;
        }
        else if(flag == MAX_FLAG){
            return getPlaceholderTitle() + MAX_KEY;
        }
        return null;
    }

    @Override
    public void initQueryValues(Query query) {
        query.setParameter(getMinOrMaxPlaceholder(MIN_FLAG),minAndMax.get(MIN_KEY));
        query.setParameter(getMinOrMaxPlaceholder(MAX_FLAG),minAndMax.get(MAX_KEY));
    }

    /**
     *
     * @param logicFlag 逻辑关系标志：1：and；2：or
     * @param key
     * @return "or(and) key between (minPlaceholder and maxPlaceholder)
     */
    protected String getBetweenHQL(@NotNull String logicFlag,@NotNull String key){
        return logicFlag + " " + key + " between (:" + getMinOrMaxPlaceholder(MIN_FLAG) + " and :" + getMinOrMaxPlaceholder(MAX_FLAG) + ")";
    }

    /**
     *
     * @param logicFlag 逻辑关系标志：1：and；2：or
     * @param key
     * @return "or(and) key not between (minPlaceholder and maxPlaceholder)
     */
    protected String getNotBetweenHQL(@NotNull String logicFlag,@NotNull String key){
        return logicFlag + " " + key + " not between (:" + getMinOrMaxPlaceholder(MIN_FLAG) + " and :" + getMinOrMaxPlaceholder(MAX_FLAG) + ")";
    }
}
