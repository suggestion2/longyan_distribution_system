package com.longyan.distribution.request;

import com.sug.core.platform.web.pagination.PaginationForm;

import javax.validation.constraints.NotNull;

public class GoldRecordBusinessListForm extends PaginationForm{
    @NotNull
    private Integer businessId;

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }
}
