package com.longyan.distribution.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class TransferParams {
    private Integer id;

    private BigDecimal amount;

    @Autowired
    public TransferParams() {
    }

    public TransferParams(Integer id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
