package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

public class InvoiceResponse {
    ValidationResult validationResults;
    String clearanceStatus;
    String clearedInvoice;
    private String invoiceHash;
    private String qrCode;

    public ValidationResult getValidationResults() {
        return validationResults;
    }

    public void setValidationResults(ValidationResult validationResults) {
        this.validationResults = validationResults;
    }

    public String getClearanceStatus() {
        return clearanceStatus;
    }

    public void setClearanceStatus(String clearanceStatus) {
        this.clearanceStatus = clearanceStatus;
    }

    public String getClearedInvoice() {
        return clearedInvoice;
    }

    public void setClearedInvoice(String clearedInvoice) {
        this.clearedInvoice = clearedInvoice;
    }

    public String getInvoiceHash() {
        return invoiceHash;
    }

    public void setInvoiceHash(String invoiceHash) {
        this.invoiceHash = invoiceHash;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
