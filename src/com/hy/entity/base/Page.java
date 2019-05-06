package com.hy.entity.base;

/**
 * @author sren
 * @create 2018-12-06 下午8:09
 **/

public class Page {

    private Integer page;

    private Integer pageSize = 10;

    private Integer categoryId;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
