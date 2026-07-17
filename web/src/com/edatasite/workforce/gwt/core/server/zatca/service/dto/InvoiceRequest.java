package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import com.edatasite.workforce.gwt.core.server.zatca.configuration.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class InvoiceRequest {
    private UUID uuid;
    private Integer id;
    private String invoiceNumber;
    private String previousInvoiceHash;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date issueDateAndTime;
    private String documentCurrencyCode;
    private String taxCurrencyCode;
    private SupplierAndCustomerParty supplier;
    private SupplierAndCustomerParty customer;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date actualDeliveryDate;
    private String paymentMeansCode;
    private ArrayList<TaxTotalItem> taxTotalList;
    private LegalManetaryTotalItem legalManetaryTotalItem;
    private ArrayList<InvoiceLineItem> invoiceLineItems;
    private String currencyCode;
    private Boolean creditNote = false;
    private String noteReason;
    private String billingReferenceId;


    private String certificate;
    private String privateKey;
    private String securityToken;
    private String secret;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPreviousInvoiceHash() {
        return previousInvoiceHash;
    }

    public void setPreviousInvoiceHash(String previousInvoiceHash) {
        this.previousInvoiceHash = previousInvoiceHash;
    }

    public Date getIssueDateAndTime() {
        return issueDateAndTime;
    }

    public void setIssueDateAndTime(Date issueDateAndTime) {
        this.issueDateAndTime = issueDateAndTime;
    }

    public String getDocumentCurrencyCode() {
        return documentCurrencyCode;
    }

    public void setDocumentCurrencyCode(String documentCurrencyCode) {
        this.documentCurrencyCode = documentCurrencyCode;
    }

    public String getTaxCurrencyCode() {
        return taxCurrencyCode;
    }

    public void setTaxCurrencyCode(String taxCurrencyCode) {
        this.taxCurrencyCode = taxCurrencyCode;
    }

    public SupplierAndCustomerParty getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierAndCustomerParty supplier) {
        this.supplier = supplier;
    }

    public SupplierAndCustomerParty getCustomer() {
        return customer;
    }

    public void setCustomer(SupplierAndCustomerParty customer) {
        this.customer = customer;
    }

    public Date getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(Date actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public String getPaymentMeansCode() {
        return paymentMeansCode;
    }

    public void setPaymentMeansCode(String paymentMeansCode) {
        this.paymentMeansCode = paymentMeansCode;
    }

    public ArrayList<TaxTotalItem> getTaxTotalList() {
        return taxTotalList;
    }

    public void setTaxTotalList(ArrayList<TaxTotalItem> taxTotalList) {
        this.taxTotalList = taxTotalList;
    }

    public LegalManetaryTotalItem getLegalManetaryTotalItem() {
        return legalManetaryTotalItem;
    }

    public void setLegalManetaryTotalItem(LegalManetaryTotalItem legalManetaryTotalItem) {
        this.legalManetaryTotalItem = legalManetaryTotalItem;
    }

    public ArrayList<InvoiceLineItem> getInvoiceLineItems() {
        return invoiceLineItems;
    }

    public void setInvoiceLineItems(ArrayList<InvoiceLineItem> invoiceLineItems) {
        this.invoiceLineItems = invoiceLineItems;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Boolean getCreditNote() {
        return creditNote;
    }

    public void setCreditNote(Boolean creditNote) {
        this.creditNote = creditNote;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getNoteReason() {
        return noteReason;
    }

    public void setNoteReason(String noteReason) {
        this.noteReason = noteReason;
    }

    public String getBillingReferenceId() {
        return billingReferenceId;
    }

    public void setBillingReferenceId(String billingReferenceId) {
        this.billingReferenceId = billingReferenceId;
    }
}
