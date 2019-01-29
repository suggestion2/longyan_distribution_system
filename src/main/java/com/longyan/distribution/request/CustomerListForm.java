package com.longyan.distribution.request;

import com.sug.core.platform.web.pagination.PaginationForm;

public class CustomerListForm extends PaginationForm{
    private String managementContent;

    public String getManagementContent() {
        return managementContent;
    }

    public void setManagementContent(String managementContent) {
        this.managementContent = managementContent;
    }
}
