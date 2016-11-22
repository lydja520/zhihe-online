package cn.com.zhihetech.online.service;

import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import cn.com.zhihetech.util.hibernate.service.SupportService;

import java.util.List;
import java.util.Map;

/**
 * 升级版Service,可分页查询指定的属性
 * Created by ShenYunjie on 2016/4/5.
 */
public interface UpgradedService<T> extends SupportService<T> {
    /**
     * 分页查询指定的某一属性
     *
     * @param selector    查询到属性
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    List<Object> getPropertyByParams(String selector, Pager pager, IQueryParams queryParams);

    /**
     * 分页查询指定的多个属性
     *
     * @param selectors   需要查询的多个属性
     * @param pager       分页
     * @param queryParams 查询参数
     * @return
     */
    List<Object[]> getPropertiesByParams(String[] selectors, Pager pager, IQueryParams queryParams);

    /**
     * 分页查询指定的某一属性
     *
     * @param selector    查询到属性
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return PageData
     */
    PageData<Object> getPropertyPageData(String selector, Pager pager, IQueryParams queryParams);

    /**
     * 分页查询指定的多个属性
     *
     * @param selectors   需要查询的多个属性
     * @param pager       分页
     * @param queryParams 查询参数
     * @return PageData
     */
    PageData<Object[]> getPropertiesPageData(String[] selectors, Pager pager, IQueryParams queryParams);

    /**
     * 执行更新
     *
     * @param values      需要更新的属性名称及属性组成的Map
     * @param queryParams 条件
     * @return
     */
    int executeUpdate(Map<String, Object> values, IQueryParams queryParams);

    /**
     * 执行删除
     *
     * @param queryParams 条件
     * @return
     */
    int executeDelete(IQueryParams queryParams);
}
