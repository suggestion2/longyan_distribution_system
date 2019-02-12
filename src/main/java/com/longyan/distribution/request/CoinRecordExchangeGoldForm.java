package com.longyan.distribution.request;

import com.sug.core.util.RegexUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class CoinRecordExchangeGoldForm {
    @NotNull
    private BigDecimal amount;
    @NotEmpty
    @Pattern(regexp = RegexUtils.REGEX_ENGNNUM,message = RegexUtils.REGEX_ENGNNUM_MESSAGE)
    @Size(min = 6,max = 12,message = "6 to 12")
    private String paymentPassword;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentPassword() {
        return paymentPassword;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }
}
