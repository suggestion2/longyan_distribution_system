package com.longyan.distribution.request;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class GoodsCategoryUpdateForm {

    @NotNull
    private Integer id;
    @NotEmpty
    private String name;
    @NotNull
    private Integer level;
    @NotNull
    private Integer parentId;

    public Integer getId() {
    return id;
    }

    public void setId(Integer id) {
    this.id = id;
    }
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