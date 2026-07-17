package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class SerialDetailItem implements IsSerializable, Serializable {
    Integer id;
    String entityType;
    String transactionNumber;
    String transactionLink;
    String transactionType;
    Boolean reversed;
    DateNonConvertable transactionDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getTransactionLink() {
        return transactionLink;
    }

    public void setTransactionLink(String transactionLink) {
        this.transactionLink = transactionLink;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Boolean getReversed() {
        return reversed != null ? reversed : false;
    }

    public void setReversed(Boolean reversed) {
        this.reversed = reversed;
    }

    public DateNonConvertable getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(DateNonConvertable transactionDate) {
        this.transactionDate = transactionDate;
    }
}
