package com.edatasite.workforce.gwt.core.client.rpc.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/16/12
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountItem extends SelectItem {
    private BigDecimal balance;
    private CurrencyItem currency;
    private BigDecimal exchangeRate;
    private String bankAccountCode;
    private String accountNumber;
    private String accountName;

    public BankAccountItem() {
    }

    public BankAccountItem(Integer id, String name) {
        super(id, name);
    }

    public BankAccountItem(Integer id, String name, BigDecimal balance, CurrencyItem currency) {
        super(id, name);
        this.balance = balance;
        this.currency = currency;
    }

    public BankAccountItem(Integer id, String name, BigDecimal balance, CurrencyItem currency, String bankAccountCode, String accountNumber) {
        super(id, name);
        this.balance = balance;
        this.currency = currency;
        this.bankAccountCode = bankAccountCode;
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getBankAccountCode() {
        return bankAccountCode;
    }

    public void setBankAccountCode(String bankAccountCode) {
        this.bankAccountCode = bankAccountCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
