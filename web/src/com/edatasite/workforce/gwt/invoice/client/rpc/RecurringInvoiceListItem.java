package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 13.05.2010
 * Time: 21:51:07
 * To change this template use File | Settings | File Templates.
 */
public class RecurringInvoiceListItem implements IsSerializable {
    public static String ACTION = "action";
    public static String CLIENT = "client";
    public static String AMOUNT = "amount";
    public static String BASE_AMOUNT = "baseAmount";
    public static String REPEATS = "repeats";
    public static String NEXT_IVOICE_DATE = "nextInvoiceDate";
    public static String END_DATE = "endDate";
    public static String STATUS = "status";
    public static String RECURRENCE_STATUS = "recurrenceStatus";
    public static String REFERENCE = "reference";
    private Integer objectId;
    private String client;
    private BigDecimal amount;
    private BigDecimal amountInInvoiceCurrency;
    private String repeats;
    private Date nextInvoiceDate;
    private Date endDate;
    private String status;
    private String statusCode;
    private String recurrenceStatus;
    private String reference;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRepeats() {
        return repeats;
    }

    public void setRepeats(String repeats) {
        this.repeats = repeats;
    }

    public Date getNextInvoiceDate() {
        return nextInvoiceDate;
    }

    public void setNextInvoiceDate(Date nextInvoiceDate) {
        this.nextInvoiceDate = nextInvoiceDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getRecurrenceStatus() {
        return recurrenceStatus;
    }

    public void setRecurrenceStatus(String recurrenceStatus) {
        this.recurrenceStatus = recurrenceStatus;
    }

    public BigDecimal getAmountInInvoiceCurrency() {
        return amountInInvoiceCurrency;
    }

    public void setAmountInInvoiceCurrency(BigDecimal amountInInvoiceCurrency) {
        this.amountInInvoiceCurrency = amountInInvoiceCurrency;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
