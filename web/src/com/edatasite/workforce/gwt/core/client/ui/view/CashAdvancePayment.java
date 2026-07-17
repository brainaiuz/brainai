package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Omonullo on 5/27/2017.
 */
public class CashAdvancePayment implements IsSerializable {

    public static final String AMOUNT = "AMOUNT";
    public static final String DATE = "DATE";
    public static final String REFERENCE = "REFERENCE";
    public static final String PERIOD = "PERIOD";

    private Integer id;
    private Integer cashadvanceId;
    private BigDecimal paymentAmount;
    private DateNonConvertable paymentDate;
    private Integer accountId;
    private String reference;
    private BigDecimal exchangeRate;
    private String period;
    public Integer getCashadvanceId() {
        return cashadvanceId;
    }

    public void setCashadvanceId(Integer cashadvanceId) {
        this.cashadvanceId = cashadvanceId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public DateNonConvertable getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(DateNonConvertable paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
