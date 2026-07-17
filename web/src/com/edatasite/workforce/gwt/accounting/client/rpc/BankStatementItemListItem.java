package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.05.2010
 * Time: 12:08:46
 * To change this template use File | Settings | File Templates.
 */
public class BankStatementItemListItem implements IsSerializable {
    public static String STATUS = "status";
    public static String DATE = "date";
    public static String DESCRIPTION = "description";
    public static String SPENT = "spent";
    public static String RECEIVED = "received";
    public static String BALANCE = "balance";
    private Date date;
    private String description;
    private BigDecimal spent;
    private BigDecimal received;
    private BigDecimal balance;
    private boolean reconsiled;
    private Integer objectID;
    private Integer bankStatementID;

    public BankStatementItemListItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getSpent() {
        return spent;
    }

    public void setSpent(BigDecimal spent) {
        this.spent = spent;
    }

    public BigDecimal getReceived() {
        return received;
    }

    public void setReceived(BigDecimal received) {
        this.received = received;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isReconsiled() {
        return reconsiled;
    }

    public void setReconsiled(boolean reconsiled) {
        this.reconsiled = reconsiled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBankStatementID() {
        return bankStatementID;
    }

    public void setBankStatementID(Integer bankStatementID) {
        this.bankStatementID = bankStatementID;
    }
}
