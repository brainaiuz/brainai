package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import java.math.BigDecimal;

public class InvoiceLineItem {
    private String lineItemId;
    private BigDecimal invoicedQuantity;
    private BigDecimal lineExtensionAmount;
    private BigDecimal taxAmount;
    private BigDecimal roundingAmount;
    private LineItemTo item;
    private BigDecimal priceAmount;

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public BigDecimal getInvoicedQuantity() {
        return invoicedQuantity;
    }

    public void setInvoicedQuantity(BigDecimal invoicedQuantity) {
        this.invoicedQuantity = invoicedQuantity;
    }

    public BigDecimal getLineExtensionAmount() {
        return lineExtensionAmount;
    }

    public void setLineExtensionAmount(BigDecimal lineExtensionAmount) {
        this.lineExtensionAmount = lineExtensionAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getRoundingAmount() {
        return roundingAmount;
    }

    public void setRoundingAmount(BigDecimal roundingAmount) {
        this.roundingAmount = roundingAmount;
    }

    public LineItemTo getItem() {
        return item;
    }

    public void setItem(LineItemTo item) {
        this.item = item;
    }

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(BigDecimal priceAmount) {
        this.priceAmount = priceAmount;
    }
}
