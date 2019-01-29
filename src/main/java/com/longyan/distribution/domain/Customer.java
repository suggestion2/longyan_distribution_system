package com.longyan.distribution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sug.core.util.jsonFormat.SimpleDateTimeSerializer;

import java.math.BigDecimal;
import java.util.Date;

public class Customer {

    private Integer id;
    private String phone;
    private String businessName;
    private String realName;
    private String businessAccount;
    private String customerBank;
    private String customerBankAccount;
    private Integer parentId;
    private Integer superParentId;
    private Integer level;
    @JsonIgnore
    private String loginPassword;
    @JsonIgnore
    private String paymentPassword;
    private BigDecimal customerGold;
    private BigDecimal customerOilDrill;
    private BigDecimal customerCoin;
    private BigDecimal businessGold;
    private BigDecimal businessOilDrill;
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
    private Integer business;
    private String remark;
    private String parentPhone;
    private String superParentPhone;
    private String parentRealName;
    private String superParentRealName;
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