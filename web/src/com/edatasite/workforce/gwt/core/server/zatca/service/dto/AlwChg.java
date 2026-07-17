package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class AlwChg {

    @SerializedName("AlwChgDiscountID")
    String alwChangeDiscountId;


    @SerializedName("Indicator")
    String Indicator;

    @SerializedName("AlwChgReason")
    String alwChgReason;

    @SerializedName("Amt")
    BigDecimal amt;

    @SerializedName("BaseAmt")
    BigDecimal baseAmt;

    @SerializedName("MFN")
    String mfn;

    public String getAlwChgReason() {
        return alwChgReason;
    }

    public void setAlwChgReason(String alwChgReason) {
        this.alwChgReason = alwChgReason;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public BigDecimal getBaseAmt() {
        return baseAmt;
    }

    public void setBaseAmt(BigDecimal baseAmt) {
        this.baseAmt = baseAmt;
    }

    public String getMfn() {
        return mfn;
    }

    public void setMfn(String mfn) {
        this.mfn = mfn;
    }

    public String getAlwChangeDiscountId() {
        return alwChangeDiscountId;
    }

    public void setAlwChangeDiscountId(String alwChangeDiscountId) {
        this.alwChangeDiscountId = alwChangeDiscountId;
    }

    public String getIndicator() {
        return Indicator;
    }

    public void setIndicator(String indicator) {
        Indicator = indicator;
    }
}
