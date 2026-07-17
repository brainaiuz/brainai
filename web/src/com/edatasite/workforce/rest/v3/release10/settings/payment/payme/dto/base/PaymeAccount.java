package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base;

import com.google.gson.annotations.SerializedName;

public class PaymeAccount {
    @SerializedName("invoice_id")
    private Long invoiceId;

    public PaymeAccount(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }
}
