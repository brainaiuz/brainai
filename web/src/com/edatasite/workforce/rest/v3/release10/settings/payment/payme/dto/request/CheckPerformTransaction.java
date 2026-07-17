package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request;

import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.PaymeAccount;

public class CheckPerformTransaction {
    private Long amount;
    private PaymeAccount account;

    public CheckPerformTransaction(Long amount, PaymeAccount account) {
        this.amount = amount;
        this.account = account;
    }

    public Long getAmount() {
        return amount;
    }

    public PaymeAccount getAccount() {
        return account;
    }
}
