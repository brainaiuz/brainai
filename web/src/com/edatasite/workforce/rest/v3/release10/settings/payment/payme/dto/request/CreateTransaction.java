package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request;

import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.PaymeAccount;

public class CreateTransaction {
    private String id;
    private Long time;
    private Long amount;
    private PaymeAccount account;

    public CreateTransaction(String id, Long time, Long amount, PaymeAccount account) {
        this.id = id;
        this.time = time;
        this.amount = amount;
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public Long getTime() {
        return time;
    }

    public Long getAmount() {
        return amount;
    }

    public PaymeAccount getAccount() {
        return account;
    }
}
