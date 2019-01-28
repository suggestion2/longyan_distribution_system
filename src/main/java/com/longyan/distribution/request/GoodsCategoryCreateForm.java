package com.longyan.distribution.request;

import com.sug.core.util.RegexUtils;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class GoodsCategoryCreateForm {

    @NotEmpty
    private String name;

    public String getName() {
    return name;
    }

    public void setName(String name) {
    this.name = name;
    }

}