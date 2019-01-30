package com.longyan.distribution.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderCreateForm {

    @NotNull
    private BigDecimal amount;
    @NotNull
    private Integer recharge;

    private String remark;

    private List<OrderItemCreateForm> list;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getRecharge() {
        return recharge;
    }

    public void setRecharge(Integer recharge) {
        this.recharge = recharge;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<OrderItemCreateForm> getList() {
        return list;
    }

    public void setList(List<OrderItemCreateForm> list) {
        this.list = list;
    }
}