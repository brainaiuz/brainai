package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

public class VatAdjustmentItem implements IsSerializable {

    private DateNonConvertable date;
    private String reference;
    private BigDecimal amount;
    private SelectItem account;
    private String reason;

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
