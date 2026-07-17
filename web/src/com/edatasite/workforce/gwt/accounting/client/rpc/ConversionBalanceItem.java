package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;


public class ConversionBalanceItem implements IsSerializable {
    private Integer objectID;
    private Integer journalId;

    private Date journalDate;
    private Date postedDate;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;

    private TransactionItem[] items;


    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(final Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getJournalId() {
        return this.journalId;
    }

    public void setJournalId(final Integer journalId) {
        this.journalId = journalId;
    }

    public Date getJournalDate() {
        return this.journalDate;
    }

    public void setJournalDate(final Date journalDate) {
        this.journalDate = journalDate;
    }

    public Date getPostedDate() {
        return this.postedDate;
    }

    public void setPostedDate(final Date postedDate) {
        this.postedDate = postedDate;
    }

    public TransactionItem[] getItems() {
        return this.items;
    }

    public void setItems(final TransactionItem[] items) {
        this.items = items;
    }

    public BigDecimal getTotalDebit() {
        return this.totalDebit != null ? this.totalDebit : BigDecimal.ZERO;
    }

    public void setTotalDebit(final BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return this.totalCredit != null ? this.totalCredit : BigDecimal.ZERO;
    }

    public void setTotalCredit(final BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }
}