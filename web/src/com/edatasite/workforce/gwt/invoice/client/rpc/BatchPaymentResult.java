package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Sherzod on 7/16/2015.
 */
public class BatchPaymentResult implements IsSerializable {

    private Integer result;
    private Integer paymentId;
    private String[] duplicatedReferences;
    private String invoiceStatusCode;
    public BatchPaymentResult() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String[] getDuplicatedReferences() {
        return duplicatedReferences;
    }

    public void setDuplicatedReferences(String[] duplicatedReferences) {
        this.duplicatedReferences = duplicatedReferences;
    }

    public String getInvoiceStatusCode() {
        return invoiceStatusCode;
    }

    public void setInvoiceStatusCode(String invoiceStatusCode) {
        this.invoiceStatusCode = invoiceStatusCode;
    }
}
