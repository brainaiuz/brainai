package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class TaxTotal {
    @SerializedName("TaxAmt")
    private BigDecimal taxAmt;
    @SerializedName("RoundingAmt")
    private BigDecimal roundingAmt;

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public BigDecimal getRoundingAmt() {
        return roundingAmt;
    }

    public void setRoundingAmt(BigDecimal roundingAmt) {
        this.roundingAmt = roundingAmt;
    }
}
