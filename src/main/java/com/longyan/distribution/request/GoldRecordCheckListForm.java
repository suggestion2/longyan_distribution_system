package com.longyan.distribution.request;

import com.sug.core.platform.web.pagination.PaginationForm;

import javax.validation.constraints.NotNull;

public class GoldRecordCheckListForm extends PaginationForm {
    private Integer status;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
