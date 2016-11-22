package cn.com.zhihetech.util.hibernate.dao;

/**
 * Hibernate Dao层基础接口
 * Created by ShenYunjie on 2015/11/16.
 *
 * @version 1.0
 */

import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface SupportDao<T> extends Serializable {
    /**
     * 获取基类对应的在hql语句中的表名称
     *
     * @return
     */
    String getTableName();

    /**
     * 获取Hibernate Session对象
     *
     * @return
     */
    Session getSession();

    /**
     * 更新实体类
     *
     * @param t 已更新完成的实体类
     */
    void updateEntity(T t);

    /**
     * 保存一个实体类到数据中
     *
     * @param entity
     * @return 已持久化到的实体类
     */
    T saveEntity(T entity);

    /**
     * 保存后更新条数据，如果数据库中存在则更行，否则保存（新增）
     *
     * @param entity
     * @return
     */
    T saveOrUpdate(T entity);

    /**
     * 删除一个持久化对象
     *
     * @param entity
     */
    void deleteEntity(T entity);

    /**
     * 根据ID从数据库中获取一个实体类
     *
     * @param id
     * @return
     */
    T findEntityById(String id);

    /**
     * 根据ID从数据库中load一个实体类
     *
     * @param id
     * @return
     */
    T loadEntiryById(String id);

    /**
     * 根据查询属性获取满足条件的数据总数
     *
     * @param queryParams
     * @return
     */
    long getRecordTotal(IQueryParams queryParams);

    /**
     * 根据HQL语句获取满足条件的数据总数
     *
     * @param queryParams HQL语句对应的键值
     * @return
     */
    long getRecordTotal(String hql, Map<String, Object> queryParams);

    /**
     * 根据"queryParams"更新对应的属性值
     *
     * @param propertyAndValues key:属性名称;value:属性对应的值
     * @param queryParams
     * @return
     */
    int executeUpdate(Map<String, Object> propertyAndValues, IQueryParams queryParams);

    /**
     * 根据"queryParams"条件执行删除语句
     *
     * @param queryParams
     * @return
     */
    int executeDelete(IQueryParams queryParams);

    /**
     * 查询对应属性的值
     *
     * @param selectors   属性数组
     * @param pager       分页
     * @param queryParams 条件参数
     * @return
     */
    List<Object[]> getProperties(String[] selectors, Pager pager, IQueryParams queryParams);

    /**
     * 查询对应属性的值
     *
     * @param selector    属性
     * @param pager       分页
     * @param queryParams 条件参数
     * @return
     */
    List<Object> getProperty(String selector, Pager pager, IQueryParams queryParams);

    /**
     * 根据查询条件查询
     *
     * @param queryParams
     * @return 满足"querParams"查询条件的结果集
     */
    List<T> getEntities(IQueryParams queryParams);

    /**
     * 根据查询条件查询，并分页
     *
     * @param pager       分页参数
     * @param queryParams
     * @return 满足"querParams"查询条件并分页的结果集
     */
    List<T> getEntities(Pager pager, IQueryParams queryParams);

    /**
     * @param pager       分页参数
     * @param queryParams 查询参数
     * @return
     */
    PageData<T> getPageData(Pager pager, IQueryParams queryParams);

    /**
     * 分页获取自定义属性组的集合
     *
     * @param pager       分页参数
     * @param queryParams 查询参数
     * @param properties  需要获取的属性名称组
     * @return
     */
    PageData<Object[]> getPageDataProperties(Pager pager, IQueryParams queryParams, String... properties);

    /**
     * 分页获取实体类的指定属性
     *
     * @param property    属性名称
     * @param pager       分页参数
     * @param queryParams 查询参数
     * @return
     */
    PageData<Object> getPageDataProperty(String property, Pager pager, IQueryParams queryParams);
}
