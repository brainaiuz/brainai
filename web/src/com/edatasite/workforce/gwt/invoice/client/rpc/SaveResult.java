package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 17.03.12
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public class SaveResult implements IsSerializable {
    private Integer id;
    private String number;
    private BigDecimal remainingBalance;
    private BigDecimal creditLimit;
    private Boolean invoiceExist;
    private Boolean paymentExist;
    private Boolean exceededCreditLimit;
    private Boolean restrictCreatingOrUpdatingInvoices;
    private Boolean hasStockValidation;
    private Integer fixedAssetID;
    private Boolean unallocatedExpensesExist;
    private String message;
    private String pdfTemplate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Boolean isInvoiceExist() {
        return invoiceExist != null ? invoiceExist : false;
    }

    public void setInvoiceExist(Boolean invoiceExist) {
        this.invoiceExist = invoiceExist;
    }

    public Boolean isPaymentExist() {
        return paymentExist != null ? paymentExist : false;
    }

    public void setPaymentExist(Boolean paymentExist) {
        this.paymentExist = paymentExist;
    }

    public Boolean getExceededCreditLimit() {
        return exceededCreditLimit != null ? exceededCreditLimit : Boolean.FALSE;
    }

    public void setExceededCreditLimit(Boolean exceededCreditLimit) {
        this.exceededCreditLimit = exceededCreditLimit;
    }

    public Boolean isRestrictCreatingOrUpdatingInvoices() {
        return restrictCreatingOrUpdatingInvoices;
    }

    public void setRestrictCreatingOrUpdatingInvoices(Boolean restrictCreatingOrUpdatingInvoices) {
        this.restrictCreatingOrUpdatingInvoices = restrictCreatingOrUpdatingInvoices;
    }

    public Integer getFixedAssetID() {
        return fixedAssetID;
    }

    public void setFixedAssetID(Integer fixedAssetID) {
        this.fixedAssetID = fixedAssetID;
    }

    public Boolean isUnallocatedExpensesExist() {
        return unallocatedExpensesExist != null ? unallocatedExpensesExist : false;
    }

    public void setUnallocatedExpensesExist(Boolean unallocatedExpensesExist) {
        this.unallocatedExpensesExist = unallocatedExpensesExist;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean hasStockValidation() {
        return hasStockValidation != null ? hasStockValidation : false;
    }

    public void setHasStockValidation(Boolean hasStockValidation) {
        this.hasStockValidation = hasStockValidation;
    }

    public String getPdfTemplate() {
        return pdfTemplate;
    }

    public void setPdfTemplate(String pdfTemplate) {
        this.pdfTemplate = pdfTemplate;
    }
}
