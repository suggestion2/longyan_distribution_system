package com.longyan.distribution.request;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class GoldRecordUpdateForm {

    @NotNull
    private Integer id;
    @NotNull
    private Integer customerId;
    @NotNull
    private Integer businessId;
    @NotNull
    private BigDecimal amount;
    @NotEmpty
    private String businessName;
    @NotEmpty
    private String businessAccount;
    @NotEmpty
    private String customerPhone;
    @NotNull
    private Integer type;
    @NotEmpty
    private String refuseReason;

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
    public Integer getBusinessId() {
    return businessId;
    }

    public void setBusinessId(Integer businessId) {
    this.businessId = businessId;
    }
    public BigDecimal getAmount() {
    return amount;
    }

    public void setAmount(BigDecimal amount) {
    this.amount = amount;
    }
    public String getBusinessName() {
    return businessName;
    }

    public void setBusinessName(String businessName) {
    this.businessName = businessName;
    }
    public String getBusinessAccount() {
    return businessAccount;
    }

    public void setBusinessAccount(String businessAccount) {
    this.businessAccount = businessAccount;
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

}