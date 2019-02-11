package com.longyan.distribution.request;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class GoldRecordStatusForm {
    @NotNull
    private Integer id;
    @NotNull
    private Integer status;
    @NotNull
    private BigDecimal applyCount;//申请提现金币数

    private String refuseReason;

    public BigDecimal getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(BigDecimal applyCount) {
        this.applyCount = applyCount;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
