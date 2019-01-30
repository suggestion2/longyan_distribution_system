package com.longyan.distribution.request;


import com.sug.core.platform.web.pagination.PaginationForm;

public class CustomerManagementlistForm extends PaginationForm {
    private Integer type;

    private String managementContent;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getManagementContent() {
        return managementContent;
    }

    public void setManagementContent(String managementContent) {
        this.managementContent = managementContent;
    }
}
