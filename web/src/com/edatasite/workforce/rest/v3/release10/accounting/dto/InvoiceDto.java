package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Normurod Buriev.
 * Date: 7/23/2020 5:02 PM
 */
public class InvoiceDto extends BaseInvoiceDto {
    @JsonProperty("isCreditNote")
    private boolean creditNote;
    @JsonAlias({"order", "quote"})
    private IdCode order;
    @JsonAlias({"accountsReceivable", "accountsPayable"})
    private IdCode accountsReceivable;
    @JsonProperty("creditedInvoice")
    private IdCode creditedInvoice;

    @Valid
    private List<InvoicePaymentDto> payments;

    public InvoiceDto() {
    }

    @JsonIgnore
    public void setQuoteNumber(String quoteNumber) {
        addProperty(QUOTE_NUMBER, quoteNumber);
    }

    @JsonIgnore
    public void setConvertedItem(IdCode object) {
        addProperty(CONVERTED_ITEM, object);
    }

    @JsonIgnore
    public void setPaidAmount(BigDecimal paidAmount) {
        addProperty(PAID_AMOUNT, paidAmount);
    }

    @JsonIgnore
    public void setDueAmount(BigDecimal dueAmount) {
        addProperty(DUE_AMOUNT, dueAmount);
    }

    @JsonIgnore
    public void setRefunds(List<InvoicePaymentDto> refunds) {
        addProperty(REFUNDS, refunds);
    }

    public boolean isCreditNote() {
        return creditNote;
    }

    public void setCreditNote(boolean creditNote) {
        this.creditNote = creditNote;
    }

    public IdCode getOrder() {
        return order;
    }

    public void setOrder(IdCode order) {
        this.order = order;
    }

    public IdCode getAccountsReceivable() {
        return accountsReceivable;
    }

    public void setAccountsReceivable(IdCode accountsReceivable) {
        this.accountsReceivable = accountsReceivable;
    }

    public IdCode getCreditedInvoice() {
        return creditedInvoice;
    }

    public void setCreditedInvoice(IdCode creditedInvoice) {
        this.creditedInvoice = creditedInvoice;
    }

    public List<InvoicePaymentDto> getPayments() {
        return payments;
    }

    public void setPayments(List<InvoicePaymentDto> payments) {
        this.payments = payments;
    }
}
