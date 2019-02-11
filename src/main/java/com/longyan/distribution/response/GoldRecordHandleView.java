package com.longyan.distribution.response;

import java.math.BigDecimal;

public class GoldRecordHandleView {
    private String phone;
    private String businessAccount;
    private String businessName;
    private String realName;
    private String customerBank;
    private String customerBankAccount;
    private BigDecimal businessGold;
    private BigDecimal applyCount;//申请提现金币数
    private BigDecimal exchangeCash;//能兑换的现金金额
    private BigDecimal handleMoney;//手续费

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessAccount() {
        return businessAccount;
    }

    public void setBusinessAccount(String businessAccount) {
        this.businessAccount = businessAccount;
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

    public BigDecimal getBusinessGold() {
        return businessGold;
    }

    public void setBusinessGold(BigDecimal businessGold) {
        this.businessGold = businessGold;
    }

    public BigDecimal getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(BigDecimal applyCount) {
        this.applyCount = applyCount;
    }

    public BigDecimal getExchangeCash() {
        return exchangeCash;
    }

    public void setExchangeCash(BigDecimal exchangeCash) {
        this.exchangeCash = exchangeCash;
    }

    public BigDecimal getHandleMoney() {
        return handleMoney;
    }

    public void setHandleMoney(BigDecimal handleMoney) {
        this.handleMoney = handleMoney;
    }
}
