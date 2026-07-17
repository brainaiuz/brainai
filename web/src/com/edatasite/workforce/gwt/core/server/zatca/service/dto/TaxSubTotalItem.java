package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import java.math.BigDecimal;

public class TaxSubTotalItem {
    private BigDecimal taxableAmount = BigDecimal.ZERO;
    private BigDecimal taxAmount = BigDecimal.ZERO;
    private TaxCategory taxCategory;

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public TaxCategory getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(TaxCategory taxCategory) {
        this.taxCategory = taxCategory;
    }
}
