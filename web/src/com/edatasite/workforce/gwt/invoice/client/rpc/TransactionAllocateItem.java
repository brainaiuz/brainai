package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 3/19/13
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransactionAllocateItem implements IsSerializable {

    private Integer objectID;
    private Integer currencyID;
    private String narration;
    private Date date;
    private BigDecimal amount;
    private String reference;
    private Integer accountID;
    private BigDecimal exchangeRate;
    private Integer crmAccountID;
    private String crmAccountName;
    private String number;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getCrmAccountID() {
        return crmAccountID;
    }

    public void setCrmAccountID(Integer crmAccountID) {
        this.crmAccountID = crmAccountID;
    }

    public String getCrmAccountName() {
        return crmAccountName;
    }

    public void setCrmAccountName(String crmAccountName) {
        this.crmAccountName = crmAccountName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
