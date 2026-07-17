package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov.
 */
public class PaymentTO implements IsSerializable {
    Integer id;
    Integer batchPaymentId;
    Long date;
    String number;
    BigDecimal amount;
    BigDecimal total;
    String reference;
    SelectItemTO currency;
    BigDecimal exchangeRate;
    SelectItemTO type;
    SelectItemTO account;
    SelectItemTO crmAccount;
    String target;
    ArrayList<PaymentTO> payments;

    public PaymentTO() {

    }

    public PaymentTO(ReceivePaymentData receivePaymentData) {
        this.id = receivePaymentData.getObjectID();

    }

    public PaymentTO(PaymentData paymentData) {
        this.id = paymentData.getObjectID();
        this.date = WrapUtils.dateToLong(paymentData.getDate());
        this.amount = paymentData.getPaymentAmount();
        this.reference = paymentData.getReferenceNumber();
        this.exchangeRate = paymentData.getExchangeRate();
        this.type = new SelectItemTO(paymentData.getType());
        if (paymentData.getAccountItem() != null) {
            this.account = new SelectItemTO(paymentData.getAccountItem().getId(), paymentData.getAccountItem().getName());
        }
        if (paymentData.getCurrency() != null) {
            this.currency = new SelectItemTO(paymentData.getCurrency());
        }
        if (paymentData.getCrmAccount() != null) {
            this.crmAccount = new SelectItemTO(paymentData.getCrmAccount());
        }
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBatchPaymentId() {
        return batchPaymentId;
    }

    public void setBatchPaymentId(Integer batchPaymentId) {
        this.batchPaymentId = batchPaymentId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public SelectItemTO getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItemTO currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public SelectItemTO getType() {
        return type;
    }

    public void setType(SelectItemTO type) {
        this.type = type;
    }

    public SelectItemTO getAccount() {
        return account;
    }

    public void setAccount(SelectItemTO account) {
        this.account = account;
    }

    public SelectItemTO getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(SelectItemTO crmAccount) {
        this.crmAccount = crmAccount;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ArrayList<PaymentTO> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<PaymentTO> payments) {
        this.payments = payments;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
