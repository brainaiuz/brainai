package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

public class TaxAmountItem implements IsSerializable {

    private BigDecimal taxableAmount;
    private BigDecimal taxAmount;
    private BigDecimal adjustment;

    public BigDecimal getTaxableAmount() {
        return taxableAmount != null ? taxableAmount : BigDecimal.ZERO;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount != null ? taxAmount : BigDecimal.ZERO;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getAdjustment() {
        return adjustment != null ? adjustment : BigDecimal.ZERO;
    }

    public void setAdjustment(BigDecimal adjustment) {
        this.adjustment = adjustment;
    }
}
