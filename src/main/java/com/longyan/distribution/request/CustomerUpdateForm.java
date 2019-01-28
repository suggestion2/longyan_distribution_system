package com.longyan.distribution.request;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CustomerUpdateForm {

    @NotNull
    private Integer id;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String businessName;
    @NotEmpty
    private String realName;
    @NotEmpty
    private String businessAccount;
    @NotEmpty
    private String customerBank;
    @NotEmpty
    private String customerBankAccount;
    @NotNull
    private Integer parentId;
    @NotNull
    private Integer superParentId;
    @NotNull
    private Integer level;
    @NotEmpty
    private String loginPassword;
    @NotEmpty
    private String paymentPassword;
    @NotNull
    private BigDecimal customerGold;
    @NotNull
    private BigDecimal customerOilDrill;
    @NotNull
    private BigDecimal customerCoin;
    @NotNull
    private BigDecimal businessGold;
    @NotNull
    private BigDecimal businessOilDrill;
    @NotNull
    private Integer business;
    @NotEmpty
    private String remark;
    @NotEmpty
    private String parentPhone;
    @NotEmpty
    private String superParentPhone;
    @NotEmpty
    private String parentRealName;
    @NotEmpty
    private String superParentRealName;
    @NotNull
    private Integer businessStatus;

    public Integer getId() {
    return id;
    }

    public void setId(Integer id) {
    this.id = id;
    }
    public String getPhone() {
    return phone;
    }

    public void setPhone(String phone) {
    this.phone = phone;
    }
    public String getBusinessName() {
    return businessName;
    }

    public void setBusinessName(String businessName) {
    this.businessName = businessName;
    }
    public String getRealName() {
    return realName;
    }

    public void setRealName(String realName) {
    this.realName = realName;
    }
    public String getBusinessAccount() {
    return businessAccount;
    }

    public void setBusinessAccount(String businessAccount) {
    this.businessAccount = businessAccount;
    }
    public String getCustomerBank() {
    return customerBank;
    }

    public void setCustomerBank(String customerBank) {
    this.customerBank = customerBank;
    }
    public String getCustomerBankAccount() {
    return customerBankAccount;
    }

    public void setCustomerBankAccount(String customerBankAccount) {
    this.customerBankAccount = customerBankAccount;
    }
    public Integer getParentId() {
    return parentId;
    }

    public void setParentId(Integer parentId) {
    this.parentId = parentId;
    }
    public Integer getSuperParentId() {
    return superParentId;
    }

    public void setSuperParentId(Integer superParentId) {
    this.superParentId = superParentId;
    }
    public Integer getLevel() {
    return level;
    }

    public void setLevel(Integer level) {
    this.level = level;
    }
    public String getLoginPassword() {
    return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
    this.loginPassword = loginPassword;
    }
    public String getPaymentPassword() {
    return paymentPassword;
    }

    public void setPaymentPassword(String paymentPassword) {
    this.paymentPassword = paymentPassword;
    }
    public BigDecimal getCustomerGold() {
    return customerGold;
    }

    public void setCustomerGold(BigDecimal customerGold) {
    this.customerGold = customerGold;
    }
    public BigDecimal getCustomerOilDrill() {
    return customerOilDrill;
    }

    public void setCustomerOilDrill(BigDecimal customerOilDrill) {
    this.customerOilDrill = customerOilDrill;
    }
    public BigDecimal getCustomerCoin() {
    return customerCoin;
    }

    public void setCustomerCoin(BigDecimal customerCoin) {
    this.customerCoin = customerCoin;
    }
    public BigDecimal getBusinessGold() {
    return businessGold;
    }

    public void setBusinessGold(BigDecimal businessGold) {
    this.businessGold = businessGold;
    }
    public BigDecimal getBusinessOilDrill() {
    return businessOilDrill;
    }

    public void setBusinessOilDrill(BigDecimal businessOilDrill) {
    this.businessOilDrill = businessOilDrill;
    }
    public Integer getBusiness() {
    return business;
    }

    public void setBusiness(Integer business) {
    this.business = business;
    }
    public String getRemark() {
    return remark;
    }

    public void setRemark(String remark) {
    this.remark = remark;
    }
    public String getParentPhone() {
    return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
    this.parentPhone = parentPhone;
    }
    public String getSuperParentPhone() {
    return superParentPhone;
    }

    public void setSuperParentPhone(String superParentPhone) {
    this.superParentPhone = superParentPhone;
    }
    public String getParentRealName() {
    return parentRealName;
    }

    public void setParentRealName(String parentRealName) {
    this.parentRealName = parentRealName;
    }
    public String getSuperParentRealName() {
    return superParentRealName;
    }

    public void setSuperParentRealName(String superParentRealName) {
    this.superParentRealName = superParentRealName;
    }
    public Integer getBusinessStatus() {
    return businessStatus;
    }

    public void setBusinessStatus(Integer businessStatus) {
    this.businessStatus = businessStatus;
    }

}