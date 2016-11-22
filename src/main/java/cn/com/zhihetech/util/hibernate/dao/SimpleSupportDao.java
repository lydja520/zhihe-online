package cn.com.zhihetech.util.hibernate.dao;

import cn.com.zhihetech.util.common.GenericTypeUtils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.exception.UpdateParamsException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/11/16.
 */
public class SimpleSupportDao<T> extends AbstractSupportDao<T> {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public String getTableName() {
        return GenericTypeUtils.getGenerParamType(this.getClass()).getSimpleName();
    }

    @Override
    public final Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public void updateEntity(T entity) {
        this.getSession().update(entity);
    }

    @Override
    public T saveEntity(T entity) {
        getSession().save(entity);
        return entity;
    }

    @Override
    public T saveOrUpdate(T entity) {
        this.getSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void deleteEntity(T entity) {
        getSession().delete(entity);
    }

    @Override
    public T findEntityById(String id) {
        return (T) getSession().get(GenericTypeUtils.getGenerParamType(this.getClass()), id);
    }

    @Override
    public T loadEntiryById(String id) {
        return (T) getSession().load(GenericTypeUtils.getGenerParamType(this.getClass()), id);
    }

    @Override
    public long getRecordTotal(IQueryParams queryParams) {
        StringBuffer hql = new StringBuffer();
        String idField = getIDFieldName();
        if (idField != null) {
            hql.append("select count(").append(idField).append(") from ");
        } else {
            hql.append("select count(*) from ");
        }
        hql.append(getTableName()).append(" ").append(getWhereHqlWithOutSort(queryParams));
        Query query = createQueryWithHQL(hql.toString());
        initQueryWithQueryParams(query, queryParams);
        Object obj = query.uniqueResult();
        Long total = obj == null ? 0L : Long.valueOf(String.valueOf(obj));
        return total;
    }

    @Override
    public long getRecordTotal(String hql, Map<String, Object> queryParams) {
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        initQueryValues(query, queryParams);
        Object obj = query.uniqueResult();
        Long total = obj == null ? 0L : Long.valueOf(String.valueOf(obj));
        return total;
    }

    @Override
    public int executeUpdate(Map<String, Object> propertyAndValues, IQueryParams queryParams) {
        if (propertyAndValues == null || propertyAndValues.isEmpty()) {
            throw new UpdateParamsException("update param and value not able null");
        }
        StringBuffer hql = new StringBuffer("update ").append(getTableName());
        int index = 0;
        for (String key : propertyAndValues.keySet()) {
            if (StringUtils.isEmpty(key)) {
                throw new UpdateParamsException("update param name not able null");
            }
            if (index == 0) {
                hql.append(" set ");
            } else {
                hql.append(",");
            }
            hql.append(key).append(" = :").append(key);
            index++;
        }
        hql.append(" ").append(getWhereHql(queryParams));
        Query query = createQueryWithHQL(hql.toString(), false);
        initQueryWithQueryParams(query, queryParams);

        initQueryValues(query, propertyAndValues);
        return query.executeUpdate();
    }

    @Override
    public int executeDelete(IQueryParams queryParams) {
        StringBuffer hql = new StringBuffer("delete ").append(getQueryHql(queryParams));
        Query query = createQueryWithHQL(hql.toString(), false);
        initQueryWithQueryParams(query, queryParams);
        return query.executeUpdate();
    }

    @Override
    public List<Object[]> getProperties(String[] selectors, Pager pager, IQueryParams queryParams) {
        if (selectors == null || selectors.length < 1) {
            throw new UpdateParamsException("select properties not able null");
        }
        StringBuffer hql = new StringBuffer("select ");
        int index = 0;
        for (String selector : selectors) {
            if (StringUtils.isEmpty(selector)) {
                throw new UpdateParamsException("select params not able null");
            }
            if (index == 0) {
                hql.append(selector);
            } else {
                hql.append(",").append(selector);
            }
            index++;
        }
        hql.append(" ").append(getQueryHql(queryParams));
        Query query = createQueryWithHQL(hql.toString());
        initQueryWithQueryParams(query, queryParams);
        initQueryPage(query, pager);
        return query.list();
    }

    @Override
    public List<Object> getProperty(String selector, Pager pager, IQueryParams queryParams) {
        if (StringUtils.isEmpty(selector)) {
            throw new UpdateParamsException("select property not able null");
        }
        StringBuffer hql = new StringBuffer("select ").append(selector).append(" ").append(getQueryHql(queryParams));
        Query query = createQueryWithHQL(hql.toString());
        initQueryWithQueryParams(query, queryParams);
        initQueryPage(query, pager);
        return query.list();
    }

    @Override
    public List<T> getEntities(IQueryParams queryParams) {
        Query query = createQuery(queryParams);
        initQueryWithQueryParams(query, queryParams);
        return query.list();
    }

    @Override
    public List<T> getEntities(Pager pager, IQueryParams queryParams) {
        Query query = createQuery(queryParams);
        initQueryPage(query, pager);
        initQueryWithQueryParams(query, queryParams);
        return query.list();
    }

    @Override
    public PageData<T> getPageData(Pager pager, IQueryParams queryParams) {
        long total = getRecordTotal(queryParams);
        Query query = createQuery(queryParams);
        initQueryPage(query, pager);
        initQueryWithQueryParams(query, queryParams);
        PageData<T> pageData = new PageData<>(total, pager);
        pageData.setRows(query.list());
        return pageData;
    }

    /**
     * 分页获取自定义属性
     *
     * @param pager       分页参数
     * @param queryParams 查询参数
     * @param properties  需要获取的属性名称数组
     * @return
     */
    @Override
    public PageData<Object[]> getPageDataProperties(Pager pager, IQueryParams queryParams, String... properties) {
        long total = getRecordTotal(queryParams);
        List<Object[]> result = this.getProperties(properties, pager, queryParams);
        PageData<Object[]> pageData = new PageData<>(total, pager);
        pageData.setRows(result);
        return pageData;
    }

    /**
     * 分页获取实体类的指定属性
     *
     * @param property    属性名称
     * @param pager       分页参数
     * @param queryParams 查询参数
     * @return
     */
    @Override
    public PageData<Object> getPageDataProperty(String property, Pager pager, IQueryParams queryParams) {
        long total = getRecordTotal(queryParams);
        List<Object> result = this.getProperty(property, pager, queryParams);
        PageData<Object> pageData = new PageData<>(total, pager);
        pageData.setRows(result);
        return pageData;
    }
}
