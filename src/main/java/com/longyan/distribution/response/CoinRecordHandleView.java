package com.longyan.distribution.response;

import java.math.BigDecimal;

public class CoinRecordHandleView {
    private String phone;
    private String businessAccount;
    private String businessName;
    private String realName;
    private String customerBank;
    private String customerBankAccount;
    private BigDecimal customerCoin;
    private BigDecimal applyCount;//申请提现钢镚数
    private BigDecimal exchangeCash;//能兑换的现金金额
    private BigDecimal handleMoney;//手续费
    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BigDecimal getCustomerCoin() {
        return customerCoin;
    }

    public void setCustomerCoin(BigDecimal customerCoin) {
        this.customerCoin = customerCoin;
    }

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
