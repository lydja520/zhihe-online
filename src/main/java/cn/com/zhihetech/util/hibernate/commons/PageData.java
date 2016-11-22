package cn.com.zhihetech.util.hibernate.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/16.
 */
public class PageData<T> implements Serializable, Cloneable {
    private long total; //数据总数
    private int page;   //当前第几页
    private int pageSize;   //每页显示数据条数
    private int totalPage;  //数据总共页数
    private List<T> rows = new ArrayList<>();   //数据

    public PageData() {
        super();
    }

    public PageData(long total, Pager pager) {
        this.page = pager.getPage();
        this.pageSize = pager.getRows();
        this.total = total;
        this.totalPage = (int) (total / this.pageSize);
        if (total % this.pageSize != 0) {
            this.totalPage++;
        }
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public PageData<T> setTotalPage(int totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public List<T> getRows() {
        return rows;
    }

    public PageData<T> setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }
}
