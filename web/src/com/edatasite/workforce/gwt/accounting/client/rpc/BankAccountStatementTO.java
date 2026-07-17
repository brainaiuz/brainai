package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User: Anvarbek
 * Date: May 13, 2010
 * Time: 6:18:33 PM
 */
public class BankAccountStatementTO implements IsSerializable {

    private boolean debitCredit;
    private Integer bankStatementItemID;

    private Integer objectID;

    private Date transactionDate;
    private String description;
    private String reference;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal balance;
    private Integer bankGlAccountID;
    private Integer bankAccountID;
    private boolean reconsiled;
    private String accountCode;

    private String matchResult;

    private Transaction transaction;


    public BankAccountStatementTO() {
    }

    public boolean isDebitCredit() {
        return debitCredit;
    }

    public void setDebitCredit(boolean debitCredit) {
        this.debitCredit = debitCredit;
    }

    public Integer getBankStatementItemID() {
        return bankStatementItemID;
    }

    public void setBankStatementItemID(Integer bankStatementItemID) {
        this.bankStatementItemID = bankStatementItemID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public Integer getBankGlAccountID() {
        return bankGlAccountID;
    }

    public void setBankGlAccountID(Integer bankGlAccountID) {
        this.bankGlAccountID = bankGlAccountID;
    }

    public Integer getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(Integer bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAmount() {
        return getDebit() != null && getDebit().compareTo(BigDecimal.ZERO) > 0 ? getDebit() : getCredit();
    }

    public boolean isReconsiled() {
        return reconsiled;
    }

    public void setReconsiled(boolean reconsiled) {
        this.reconsiled = reconsiled;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
