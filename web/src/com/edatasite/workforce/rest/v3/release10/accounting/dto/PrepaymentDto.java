package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PrepaymentDto {

    private Integer id;

    @NotNull(message = "Customer/Supplier Id is required.")
    private Integer crmAccountId;

    @NotNull(message = "Payment Account Id is required.")
    private Integer paymentAccountId;

    @NotNull(message = "Payment Amount is required.")
    private BigDecimal paymentAmount;

    @NotNull(message = "Type is required.")
    private String type;

    private Integer rentalOrderId;

    private Integer invoiceId;

    private Integer quoteId;

    private Integer purchaseOrderId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCrmAccountId() {
        return crmAccountId;
    }

    public void setCrmAccountId(Integer crmAccountId) {
        this.crmAccountId = crmAccountId;
    }

    public Integer getPaymentAccountId() {
        return paymentAccountId;
    }

    public void setPaymentAccountId(Integer paymentAccountId) {
        this.paymentAccountId = paymentAccountId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRentalOrderId() {
        return rentalOrderId;
    }

    public void setRentalOrderId(Integer rentalOrderId) {
        this.rentalOrderId = rentalOrderId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
}
