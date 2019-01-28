package com.longyan.distribution.request;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class GoodsCategoryCreateForm {

    @NotEmpty
    private String name;
    @NotNull
    private Integer level;
    @NotNull
    private Integer parentId;

    public String getName() {
    return name;
    }

    public void setName(String name) {
    this.name = name;
    }
    public Integer getLevel() {
    return level;
    }

    public void setLevel(Integer level) {
    this.level = level;
    }
    public Integer getParentId() {
    return parentId;
    }

    public void setParentId(Integer parentId) {
    this.parentId = parentId;
    }

}