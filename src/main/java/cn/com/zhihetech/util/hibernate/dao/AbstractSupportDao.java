package cn.com.zhihetech.util.hibernate.dao;

import cn.com.zhihetech.util.common.GenericTypeUtils;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.exception.HqlException;
import org.hibernate.Query;
import org.hibernate.annotations.Cache;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by ShenYunjie on 2015/11/16.
 */
public abstract class AbstractSupportDao<T> implements SupportDao<T> {
    /**
     * 根据条件参数创建Query对象，默认不使用二级缓存
     *
     * @param queryParams
     * @return
     */

    protected Query createQuery(IQueryParams queryParams) {
        return createQuery(queryParams, isCacheable());
    }

    /**
     * 根据HQL语句创建Query对象,默认不启用二级缓存
     *
     * @param hql
     * @return
     */
    protected Query createQueryWithHQL(String hql) {
        return createQueryWithHQL(hql, isCacheable());
    }

    /**
     * 根据条件参数创建Query对象
     *
     * @param queryParams 条件参数
     * @param cacheable   是否启用二级缓存
     * @return
     */
    protected Query createQuery(IQueryParams queryParams, boolean cacheable) {
        return createQueryWithHQL(this.getQueryHql(queryParams), cacheable);
    }

    /**
     * 根据HQL语句创建Query对象
     *
     * @param hql
     * @param cacheable 是否启用二级缓存
     * @return
     */
    protected Query createQueryWithHQL(String hql, boolean cacheable) {
        Query query = getSession().createQuery(hql);
        if (cacheable) {
            query.setCacheable(cacheable);
        }
        return query;
    }

    /**
     * 根据条件参数创建Query对象，不包括排序语句不启用二级缓存
     *
     * @param queryParams 条件参数
     * @return
     */
    protected Query createQueryWithoutSort(IQueryParams queryParams) {
        return createQueryWithoutSort(queryParams, isCacheable());
    }

    /**
     * 根据条件参数创建Query对象，不包括排序语句
     *
     * @param queryParams 条件参数
     * @param cacheable   是否启用二级缓存
     * @return
     */
    protected Query createQueryWithoutSort(IQueryParams queryParams, boolean cacheable) {
        return createQueryWithHQL(this.getQueryHqlWithOutSort(queryParams), cacheable);
    }

    /**
     * 根据Map对象初始化Query对象的值
     *
     * @param query  查询对象
     * @param params "query"对应值
     */
    protected void initQueryValues(Query query, Map<String, Object> params) {
        for (String key : params.keySet()) {
            if (StringUtils.isEmpty(key)) {
                throw new HqlException("hql placeholder not able null");
            }
            query.setParameter(key, params.get(key));
        }
    }

    /**
     * 初始化Query对象的值
     *
     * @param query       Hibernate查询对象
     * @param queryParams 查询条件参数
     */
    protected void initQueryWithQueryParams(Query query, IQueryParams queryParams) {
        if (queryParams != null) {
            queryParams.initQueryParamsValue(query);
        }
    }

    /**
     * 初始化Query对象分页
     *
     * @param query
     * @param pager
     */
    protected void initQueryPage(Query query, Pager pager) {
        if (pager == null) {
            return;
        }
        query.setFirstResult(pager.getFirstIndex());
        query.setMaxResults(pager.getRows());
    }

    /**
     * 根据查询条件获取对应的包含排序子句的查询HQL语句
     *
     * @param queryParams
     * @return "from xx where 1=1 ... order by ..."
     */
    protected String getQueryHql(IQueryParams queryParams) {
        StringBuffer hql = new StringBuffer().append("from ").append(getTableName())
                .append(" ").append(getWhereHql(queryParams));
        System.out.println(hql);
        return hql.toString().trim();
    }

    /**
     * 根据查询条件获取对应的不包含排序的查询HQL语句
     *
     * @param queryParams
     * @return "from xx where 1=1 ..."
     */
    protected String getQueryHqlWithOutSort(IQueryParams queryParams) {
        StringBuffer hql = new StringBuffer().append("from ").append(getTableName())
                .append(" ").append(getWhereHqlWithOutSort(queryParams));
        return hql.toString().trim();
    }

    /**
     * 根据查询条件获取不带from关键字，包含排序的HQL语句
     *
     * @param queryParams
     * @return "where 1=1 and ... order by ..."
     */
    protected String getWhereHql(IQueryParams queryParams) {
        StringBuffer hql = new StringBuffer().append(" where 1 = 1").append(" ")
                .append(getParamHql(queryParams));
        return hql.toString().trim();
    }

    /**
     * 根据查询条件获取不带from关键字，不包含排序的HQL语句
     *
     * @param queryParams
     * @return "where 1=1 and ..."
     */
    protected String getWhereHqlWithOutSort(IQueryParams queryParams) {
        StringBuffer hql = new StringBuffer().append(" where 1 = 1").append(" ")
                .append(getParamHql(queryParams, false));
        return hql.toString().trim();
    }

    /**
     * 获取查询条件对应的包含排序子句的HQL语句
     *
     * @param queryParams
     * @return
     */
    protected String getParamHql(IQueryParams queryParams) {
        return getParamHql(queryParams, true);
    }

    /**
     * 获取查询条件对应的HQL语句，对应的HQL语句没有"from"和"where"关键字
     *
     * @param queryParams
     * @param sortable    是否需要排序语句
     * @return
     */
    protected String getParamHql(IQueryParams queryParams, boolean sortable) {
        if (queryParams == null) {
            return "";
        }
        if (sortable) {
            return queryParams.getTargetHQL();
        } else {
            return queryParams.getWithOutOrderTargetHQL();
        }
    }

    /**
     * 是否需要启用二级缓存
     *
     * @return
     */
    private boolean isCacheable() {
        Annotation[] annotations = GenericTypeUtils.getGenerParamType(this.getClass()).getAnnotations();
        boolean cacheable = false;
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Cache) {
                    cacheable = true;
                }
            }
        }
        return cacheable;
    }

    /**
     * 获取实体类的ID属性名称
     *
     * @return
     */
    protected String getIDFieldName() {
        Class clazz = GenericTypeUtils.getGenerParamType(this.getClass());
        Method[] methods = clazz.getMethods();
        if (methods == null || methods.length < 1) {
            return null;
        }
        for (Method method : methods) {
            Annotation annotation = method.getAnnotation(javax.persistence.Id.class);
            if (annotation == null) {
                continue;
            }
            String methodName = method.getName();
            if (!methodName.startsWith("get")) {
                return null;
            }
            String fieldName = methodName.substring(3);
            fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
            return fieldName;
        }
        return null;
    }
}
