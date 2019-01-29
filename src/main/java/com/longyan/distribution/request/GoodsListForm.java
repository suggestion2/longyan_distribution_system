package com.longyan.distribution.request;

import com.sug.core.platform.web.pagination.PaginationForm;

public class GoodsListForm extends PaginationForm{
    private String content;

    private Integer categoryId1;

    private Integer status;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(Integer categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
