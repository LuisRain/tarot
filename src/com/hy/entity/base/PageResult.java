package com.hy.entity.base;

import java.util.List;

/**
 * @author sren
 * @create 2018-12-06 下午9:13
 **/

public class PageResult<E> {

    private Long totalPage;

    private int page;

    private int pageSize;

    private List<E> data;

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
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

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }
}
