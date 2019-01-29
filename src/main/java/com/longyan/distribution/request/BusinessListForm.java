package com.longyan.distribution.request;


import com.sug.core.platform.web.pagination.PaginationForm;

public class BusinessListForm extends PaginationForm {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
