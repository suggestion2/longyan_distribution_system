package com.longyan.distribution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sug.core.util.jsonFormat.SimpleDateTimeSerializer;

import java.math.BigDecimal;
import java.util.Date;

public class CoinRecord {

    private Integer id;
    private Integer customerId;
    private Integer sourceCustomerId;
    private BigDecimal amount;
    private String customerPhone;
    @JsonSerialize(using = SimpleDateTimeSerializer.class)
    private Date createTime;
    @JsonIgnore
    private Date updateTime;
    @JsonIgnore
    private Integer createBy;
    @JsonIgnore
    private Integer updateBy;
    private Integer status;
    @JsonIgnore
    private Integer valid;
    private Integer type;
    private String refuseReason;
    private Integer sourceCustomerLevel;
    private String sourceCustomerPhone;
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public Integer getSourceCustomerId() {
        return sourceCustomerId;
    }

    public void setSourceCustomerId(Integer sourceCustomerId) {
        this.sourceCustomerId = sourceCustomerId;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }
    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }
    public Integer getSourceCustomerLevel() {
        return sourceCustomerLevel;
    }

    public void setSourceCustomerLevel(Integer sourceCustomerLevel) {
        this.sourceCustomerLevel = sourceCustomerLevel;
    }
    public String getSourceCustomerPhone() {
        return sourceCustomerPhone;
    }

    public void setSourceCustomerPhone(String sourceCustomerPhone) {
        this.sourceCustomerPhone = sourceCustomerPhone;
    }

}