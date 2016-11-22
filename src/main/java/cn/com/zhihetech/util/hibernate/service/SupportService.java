package cn.com.zhihetech.util.hibernate.service;

import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/17.
 */
public interface SupportService<T> extends Serializable {
    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    T getById(String id);

    /**
     * 删除持久化对象
     *
     * @param t 需要删除的持久化对象
     */
    void delete(T t);

    /**
     * 添加一个对象到数据库
     *
     * @param t 需要持久化的对象
     * @return
     */
    T add(T t);

    /**
     * 更新一个持久化对象
     *
     * @param t
     */
    void update(T t);

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    List<T> getAllByParams(Pager pager, IQueryParams queryParams);

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    PageData<T> getPageData(Pager pager, IQueryParams queryParams);
}
