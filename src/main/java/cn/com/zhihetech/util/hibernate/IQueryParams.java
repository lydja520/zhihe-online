package cn.com.zhihetech.util.hibernate;

import cn.com.zhihetech.util.hibernate.param.IParam;
import cn.com.zhihetech.util.hibernate.param.PropertyParam;
import com.sun.istack.internal.NotNull;
import org.hibernate.Query;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by ShenYunjie on 2015/11/12.
 * hibernate查询参数类
 *
 * @version 1.0
 */
public interface IQueryParams extends Serializable, Cloneable {

    final String SUB_QUERY_PARAMS = "_sub_query";

    /**
     * 且key=value
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams andEqual(@NotNull String key, @NotNull Object value);

    /**
     * 且不等于
     *
     * @param key   属性名称
     * @param value 属性值
     * @return
     */
    IQueryParams andNotEq(@NotNull String key, @NotNull Object value);

    /**
     * 且大于
     *
     * @param key   属性名称
     * @param value 属性值
     * @return
     */
    IQueryParams andMoreThan(@NotNull String key, @NotNull Object value);

    /**
     * 且小于
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams andLessThan(@NotNull String key, @NotNull Object value);

    /**
     * 且大于等于
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams andMoreAndEq(@NotNull String key, @NotNull Object value);

    /**
     * 且小于等于
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams andLessAndEq(@NotNull String key, @NotNull Object value);

    /**
     * and key is null
     *
     * @param key
     * @return
     */
    IQueryParams andIsNull(@NotNull String key);

    /**
     * and key is not null
     *
     * @param key
     * @return
     */
    IQueryParams andNotNull(@NotNull String key);

    /**
     * and key between in (minValue and maxValue)
     *
     * @param key
     * @param minValue 起始值
     * @param maxValue 终点值
     * @return and key between in (minValue and maxValue)
     */
    IQueryParams andBetween(@NotNull String key, @NotNull Object minValue, @NotNull Object maxValue);

    /**
     * 且在collection中，类似于 " and key in (x1,x2,x3,x4)"
     *
     * @param key
     * @param collection
     * @return
     */
    IQueryParams andIn(@NotNull String key, @NotNull Collection<Object> collection);

    /**
     * 且不在在collection中，类似于 " and key not in (x1,x2,x3,x4)"
     *
     * @param key
     * @param collection
     * @return
     */
    IQueryParams andNotIn(@NotNull String key, @NotNull Collection<Object> collection);

    /**
     * 类似于" and key like 'value%'"
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams andRLike(@NotNull String key, String value);

    /**
     * 类似于" and key like '%value'"
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams andLLike(@NotNull String key, String value);

    /**
     * 类似于" and key like '%value%'"
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams andAllLike(@NotNull String key, String value);

    /**
     * and (x1 = xx1 and x2 = xx2 or x3 = xx3)
     *
     * @param params
     * @return IQueryParams
     */
    IQueryParams andSubParams(SubQueryParams params);

    /**
     * and proParam(xx = aaa)
     *
     * @param proParam
     * @return IQueryParams
     */
    IQueryParams andProParam(@NotNull String proParam);

    /**
     * or key=value
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams orEqual(@NotNull String key, @NotNull Object value);

    /**
     * or key != value
     *
     * @param key   属性名称
     * @param value 属性值
     * @return
     */
    IQueryParams orNotEq(@NotNull String key, @NotNull Object value);

    /**
     * or key > value
     *
     * @param key   属性名称
     * @param value 属性值
     * @return
     */
    IQueryParams orMoreThan(@NotNull String key, @NotNull Object value);

    /**
     * or key < value
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams orLessThan(@NotNull String key, @NotNull Object value);

    /**
     * or key >= value
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams orMoreAndEq(@NotNull String key, @NotNull Object value);

    /**
     * or key <= value
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams orLessAndEq(@NotNull String key, @NotNull Object value);

    /**
     * or key is null
     *
     * @param key
     * @return
     */
    IQueryParams orIsNull(@NotNull String key);

    /**
     * or key is not null
     *
     * @param key
     * @return
     */
    IQueryParams orNotNull(@NotNull String key);

    /**
     * or key between in (minValue and maxValue)
     *
     * @param key
     * @param minValue 起始值
     * @param maxValue 终点值
     * @return
     */
    IQueryParams orBetween(@NotNull String key, @NotNull Object minValue, @NotNull Object maxValue);

    /**
     * 或在collection中，类似于 " or key in (x1,x2,x3,x4)"
     *
     * @param key
     * @param collection
     * @return
     */
    IQueryParams orIn(@NotNull String key, @NotNull Collection<Object> collection);

    /**
     * or key not in (x1,x2,x3,x4)
     *
     * @param key
     * @param collection
     * @return
     */
    IQueryParams orNotIn(@NotNull String key, @NotNull Collection<Object> collection);

    /**
     * or key like 'value%'
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams orRLike(@NotNull String key, String value);

    /**
     * or key like '%value'
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams orLLike(@NotNull String key, String value);

    /**
     * or key like '%value%'
     *
     * @param key
     * @param value
     * @return
     */
    IQueryParams orAllLike(@NotNull String key, String value);

    /**
     * or (x1 = xx1 and x2 = xx2 or x3 = xx3)
     *
     * @param params
     * @return
     */
    IQueryParams orSubParams(SubQueryParams params);

    /**
     * or proParam(xx == aa)
     *
     * @param proParam
     * @return IQueryParams
     */
    IQueryParams orProParam(String proParam);

    /**
     * 添加排序要素
     *
     * @param key   需要排序的属性
     * @param order 排序方式,asc:正序、desc：倒序
     * @return
     */
    IQueryParams sort(@NotNull String key, Order order);

    /**
     * 根据查询参数获取HQL语句
     *
     * @return HQL语句
     */
    String getTargetHQL();

    /**
     * 获取没有排序语句的HQL语句
     *
     * @return
     */
    String getWithOutOrderTargetHQL();

    /**
     * 添加查询参数
     *
     * @param key   属性名称
     * @param param 查询参数值
     * @return
     */
    IQueryParams addParam(@NotNull String key, IParam param);

    /**
     * 初始化hibernate查询对象Query的值
     *
     * @param query
     */
    void initQueryParamsValue(Query query);

    /**
     * 判断当前对象是否包含某个排序属性
     *
     * @param key 需要查询的排序属性名称
     * @return
     */
    boolean sortContainsKey(String key);

    /**
     * 判断当前对象是否包含某个条件属性
     *
     * @param key 需要查询的条件属性名称
     * @return
     */
    boolean queryContainsKey(String key);
}