package com.longyan.distribution.request;

import com.sug.core.platform.web.pagination.PaginationForm;

public class CustomerListForm extends PaginationForm{
    private Integer type;

    private String Content;

    private String customerContent;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCustomerContent() {
        return customerContent;
    }

    public void setCustomerContent(String customerContent) {
        this.customerContent = customerContent;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
