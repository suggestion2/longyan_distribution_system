package com.longyan.distribution.request;


import com.sug.core.util.RegexUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerPaymentPasswordForm {
    @NotEmpty
    @Pattern(regexp = RegexUtils.REGEX_ENGNNUM,message = RegexUtils.REGEX_ENGNNUM_MESSAGE)
    @Size(min = 6,max = 12,message = "6 to 12")
    private String originPassword;
    @NotEmpty
    @Pattern(regexp = RegexUtils.REGEX_ENGNNUM,message = RegexUtils.REGEX_ENGNNUM_MESSAGE)
    @Size(min = 6,max = 12,message = "6 to 12")
    private String newPassword;

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
