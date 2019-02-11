package com.longyan.distribution.request;

import com.sug.core.platform.web.pagination.PaginationForm;

public class CustomerListForm extends PaginationForm{
    private Integer type;

    private String content;

    private String customerContent;

    private String phone;

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
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
