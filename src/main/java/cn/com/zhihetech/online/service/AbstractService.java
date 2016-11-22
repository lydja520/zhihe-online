package cn.com.zhihetech.online.service;

import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;

import java.util.List;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/4/5.
 */
public abstract class AbstractService<T> implements UpgradedService<T> {
    /**
     * 分页查询指定的某一属性
     *
     * @param selector    查询到属性
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<Object> getPropertyByParams(String selector, Pager pager, IQueryParams queryParams) {
        return null;
    }

    /**
     * 分页查询指定的多个属性
     *
     * @param selectors   需要查询的多个属性
     * @param pager       分页
     * @param queryParams 查询参数
     * @return
     */
    @Override
    public List<Object[]> getPropertiesByParams(String[] selectors, Pager pager, IQueryParams queryParams) {
        return null;
    }

    /**
     * 分页查询指定的某一属性
     *
     * @param selector    查询到属性
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return PageData
     */
    @Override
    public PageData<Object> getPropertyPageData(String selector, Pager pager, IQueryParams queryParams) {
        return null;
    }

    /**
     * 分页查询指定的多个属性
     *
     * @param selectors   需要查询的多个属性
     * @param pager       分页
     * @param queryParams 查询参数
     * @return PageData
     */
    @Override
    public PageData<Object[]> getPropertiesPageData(String[] selectors, Pager pager, IQueryParams queryParams) {
        return null;
    }

    /**
     * 执行更新
     *
     * @param values      需要更新的属性名称及属性组成的Map
     * @param queryParams 条件
     * @return
     */
    @Override
    public int executeUpdate(Map<String, Object> values, IQueryParams queryParams) {
        return 0;
    }

    /**
     * 执行删除
     *
     * @param queryParams 条件
     * @return
     */
    @Override
    public int executeDelete(IQueryParams queryParams) {
        return 0;
    }

    protected PageData createPageData(PageData pageData) {
        PageData retVal = new PageData();
        retVal.setPage(pageData.getPage());
        retVal.setPageSize(pageData.getPageSize());
        retVal.setTotal(pageData.getTotal());
        retVal.setTotalPage(pageData.getTotalPage());
        return retVal;
    }
}
