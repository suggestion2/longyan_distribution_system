package com.longyan.distribution.request;

import com.sug.core.util.RegexUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class BusinessUpdateForm {

    @NotEmpty
    @Size(min=1,max = 32)
    private String businessName;

    @NotEmpty
    @Size(min=1,max = 32)
    private String realName;
    @NotEmpty
    @Size(min=1,max = 32)
    private String customerBank;
    @NotEmpty
    @Size(min=1,max = 64)
    private String customerBankAccount;

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
}