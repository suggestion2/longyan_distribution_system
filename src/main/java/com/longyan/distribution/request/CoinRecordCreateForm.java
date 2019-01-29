package com.longyan.distribution.request;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CoinRecordCreateForm {

    @NotNull
    private Integer customerId;
    @NotNull
    private Integer sourceCustomerId;
    @NotNull
    private BigDecimal amount;
    @NotEmpty
    private String customerPhone;
    @NotNull
    private Integer type;
    @NotEmpty
    private String refuseReason;
    @NotNull
    private Integer sourceCustomerLevel;
    @NotEmpty
    private String sourceCustomerPhone;

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