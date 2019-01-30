package com.longyan.distribution.request;

import javax.validation.constraints.NotNull;

public class CustomerManagementLevelForm {
    @NotNull
    private Integer id;
    @NotNull
    private Integer level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
