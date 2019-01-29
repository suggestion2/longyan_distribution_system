package com.longyan.distribution.request;

import com.sug.core.util.RegexUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class CustomerLoginForm {
    @NotEmpty
    @Pattern(regexp=RegexUtils.REGEX_MOBILE ,message =RegexUtils.REGEX_MOBILE_MESSAGE  )
    private String phone;
    @NotEmpty
    @Pattern(regexp = RegexUtils.REGEX_ENGNNUM,message = RegexUtils.REGEX_ENGNNUM_MESSAGE)
    @Size(min = 6,max = 12,message = "6 to 12")
    private String loginPassword;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}
