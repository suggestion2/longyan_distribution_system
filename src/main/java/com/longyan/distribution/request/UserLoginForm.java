package com.longyan.distribution.request;

import com.sug.core.util.RegexUtils;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserLoginForm {

    @NotEmpty
    @Pattern(regexp = RegexUtils.REGEX_ENGNNUM,message = RegexUtils.REGEX_ENGNNUM_MESSAGE)
    @Size(min = 1,max = 12,message = "1 to 12")
    private String name;
    @NotEmpty
    @Pattern(regexp = RegexUtils.REGEX_ENGNNUM,message = RegexUtils.REGEX_ENGNNUM_MESSAGE)
    @Size(min = 6,max = 12,message = "6 to 12")
    private String password;

    public String getName() {
    return name;
    }

    public void setName(String name) {
    this.name = name;
    }
    public String getPassword() {
    return password;
    }

    public void setPassword(String password) {
    this.password = password;
    }

}