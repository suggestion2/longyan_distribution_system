package com.longyan.distribution.request;


import com.sug.core.platform.web.pagination.PaginationForm;

import javax.validation.constraints.NotNull;

public class InviteCustomerListForm extends PaginationForm {
    @NotNull
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
