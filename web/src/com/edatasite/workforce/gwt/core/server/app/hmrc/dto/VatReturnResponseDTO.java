/*
 * Copyright (c) 2023.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.core.server.app.hmrc.dto;

public class VatReturnResponseDTO {
    private String processingDate;
    private String paymentIndicator;
    private String formBundleNumber;
    private String chargeRefNumber;

    public VatReturnResponseDTO() {
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }

    public String getPaymentIndicator() {
        return paymentIndicator;
    }

    public void setPaymentIndicator(String paymentIndicator) {
        this.paymentIndicator = paymentIndicator;
    }

    public String getFormBundleNumber() {
        return formBundleNumber;
    }

    public void setFormBundleNumber(String formBundleNumber) {
        this.formBundleNumber = formBundleNumber;
    }

    public String getChargeRefNumber() {
        return chargeRefNumber;
    }

    public void setChargeRefNumber(String chargeRefNumber) {
        this.chargeRefNumber = chargeRefNumber;
    }
}
