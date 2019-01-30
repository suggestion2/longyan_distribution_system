package com.longyan.distribution.request;


import javax.validation.constraints.NotNull;

public class CustomerBusinessStatusForm {
    @NotNull
    private Integer id;
    @NotNull
    private Integer businessStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Integer businessStatus) {
        this.businessStatus = businessStatus;
    }
}
