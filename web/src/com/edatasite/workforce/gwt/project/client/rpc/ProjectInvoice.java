package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 03.09.2009
 * Time: 15:18:27
 */
public class ProjectInvoice implements IsSerializable, ListingCustomFields {
    public static String INVOICENUMBER = "invoiceNumber";
    public static String INVOICEDATE = "invoiceDate";
    public static String DUEDATE = "dueDate";
    public static String CLIENT = "client";
    public static String CURRENCY = "currency";
    public static String PROSPECTAMOUNT = "prospectAmount";
    public static String STATUS = "status";
    public static String DUEAMOUNT = "dueAmount";
    public static String PAIDAMUOUNT = "paidAmuount";
    public static String CREATOR = "creator";
    public static String TAX_TOTAL = "taxTotal";
    public static String QUOTE_NUMBER = "quoteNumber";
    public static String MANAGER = "manager";
    public static String SUB_TOTAL = "subTotal";
    public static String REFERENCE = "reference";
    public static String PO_NUMBER = "poNumber";
    public static String OPPORTUNITY_NUMBER = "opportunityNumber";
    public static String ORIGINAL_AMOUNT = "orginalAmount";
    public static String FULL_AMOUNT = "fullAmount";


    private int id;
    private String clientName;
    private String currencyName;
    private String invoiceNumber;
    private DateNonConvertable invoiceDate;
    private DateNonConvertable dueDate;
    private BigDecimal subtotal;
    private double total;
    private String status;
    private BigDecimal payments;
    private BigDecimal totalInInvoiceCurrency;
    private String creatorName;
    private BigDecimal totalTaxes;

    private BigDecimal exchageRate;
    private String managerName;
    private String quoteNumber;
    private String reference;
    private String poNumber;
    private String opportunity;
    private BigDecimal fullPayment;
    private BigDecimal fullPaymentInBase;
    //For ProjectBasedInvoice
    private Integer[] projectIDs;
    private HashMap<String, Object> customFields;

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public DateNonConvertable getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(DateNonConvertable invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public DateNonConvertable getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateNonConvertable dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer[] getProjectIDs() {
        return projectIDs;
    }

    public void setProjectIDs(Integer[] projectIDs) {
        this.projectIDs = projectIDs;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public BigDecimal getPayments() {
        return payments;
    }

    public void setPayments(BigDecimal payments) {
        this.payments = payments;
    }

    public BigDecimal getTotalInInvoiceCurrency() {
        return totalInInvoiceCurrency;
    }

    public void setTotalInInvoiceCurrency(BigDecimal totalInInvoiceCurrency) {
        this.totalInInvoiceCurrency = totalInInvoiceCurrency;
    }

    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public BigDecimal getExchageRate() {
        return exchageRate;
    }

    public void setExchageRate(BigDecimal exchageRate) {
        this.exchageRate = exchageRate;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(String opportunity) {
        this.opportunity = opportunity;
    }

    public BigDecimal getFullPayment() {
        return fullPayment;
    }

    public void setFullPayment(BigDecimal fullPayment) {
        this.fullPayment = fullPayment;
    }

    public BigDecimal getFullPaymentInBase() {
        return fullPaymentInBase;
    }

    public void setFullPaymentInBase(BigDecimal fullPaymentInBase) {
        this.fullPaymentInBase = fullPaymentInBase;
    }

    public HashMap<String, Object> getCustomFields() {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        return customFields;
    }

    public void setCustomFields(HashMap<String, Object> customFields) {
        this.customFields = customFields;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFields() != null ? getCustomFields().get(columnCodeKey) : null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFields().put(columnCodeKey, cellValue);
    }
}
