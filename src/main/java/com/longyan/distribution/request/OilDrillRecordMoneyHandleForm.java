package com.longyan.distribution.request;

import javax.validation.constraints.NotNull;

public class OilDrillRecordMoneyHandleForm {
    @NotNull
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
