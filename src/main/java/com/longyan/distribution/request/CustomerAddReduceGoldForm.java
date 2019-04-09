package com.longyan.distribution.request;


import com.sug.core.util.RegexUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CustomerAddReduceGoldForm {
    @NotNull
    private Integer id;
    @NotNull
    private Integer type;
    @NotNull
    @Pattern(regexp="(^[1-9](\\d+)?(\\.\\d{1,2})?$)|(^0$)|(^\\d\\.\\d{1,2}$)",message = RegexUtils.REGEX_NUM_MESSAGE)
    private String amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
