package com.longyan.distribution.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class GoodsCategoryStatusForm {

    @NotNull
    private Integer id;
    @NotNull
    private Integer status;

    public Integer getId() {
    return id;
    }

    public void setId(Integer id) {
    this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}