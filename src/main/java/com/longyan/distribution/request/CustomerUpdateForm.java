package com.longyan.distribution.request;

import com.sug.core.util.RegexUtils;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerUpdateForm {

    @NotEmpty
    @Pattern(regexp=RegexUtils.REGEX_CHINESE_ENG_NUM  ,message = "Must be an alphanumeric character"  )
    @Size(min=1,max = 32)
    private String realName;
    @NotEmpty
    @Pattern(regexp=RegexUtils.REGEX_CHINESE_ENG_NUM  ,message = "Must be an alphanumeric character"  )
    @Size(min=1,max = 32)
    private String customerBank;
    @NotEmpty
    @Pattern(regexp=RegexUtils.REGEX_NUM  ,message = RegexUtils.REGEX_NUM_MESSAGE  )
    private String customerBankAccount;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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