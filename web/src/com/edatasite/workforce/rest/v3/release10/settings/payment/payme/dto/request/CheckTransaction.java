package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request;

public class CheckTransaction {
    private String id;

    public CheckTransaction(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
