package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/6/14
 * Time: 10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrePaymentData implements IsSerializable {
    private CurrencyItem currency;
    private boolean enabledPostDatedTransaction;
    private boolean enabledBankMultiCurrency;
    private BankTransferNumberData bankTransferNumberData;
    private AccountItem receivablePayable;
    private PaymentData paymentData;
    private BigDecimal supplierCustomerBalance;

    public PrePaymentData() {
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public PaymentData getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(PaymentData paymentData) {
        this.paymentData = paymentData;
    }

    public boolean isEnabledPostDatedTransaction() {
        return enabledPostDatedTransaction;
    }

    public void setEnabledPostDatedTransaction(boolean enabledPostDatedTransaction) {
        this.enabledPostDatedTransaction = enabledPostDatedTransaction;
    }

    public boolean isEnabledBankMultiCurrency() {
        return enabledBankMultiCurrency;
    }

    public void setEnabledBankMultiCurrency(boolean enabledBankMultiCurrency) {
        this.enabledBankMultiCurrency = enabledBankMultiCurrency;
    }

    public BankTransferNumberData getBankTransferNumberData() {
        return bankTransferNumberData;
    }

    public void setBankTransferNumberData(BankTransferNumberData bankTransferNumberData) {
        this.bankTransferNumberData = bankTransferNumberData;
    }

    public AccountItem getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(AccountItem receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public BigDecimal getSupplierCustomerBalance() {
        return supplierCustomerBalance;
    }

    public void setSupplierCustomerBalance(BigDecimal supplierCustomerBalance) {
        this.supplierCustomerBalance = supplierCustomerBalance;
    }
}
