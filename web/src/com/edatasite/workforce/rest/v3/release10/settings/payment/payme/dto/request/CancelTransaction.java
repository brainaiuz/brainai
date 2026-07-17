package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request;

public class CancelTransaction {
    private String id;
    private Integer reason;

    public CancelTransaction() {}

    public CancelTransaction(String id, Integer reason) {
        this.id = id;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public Integer getReason() {
        return reason;
    }
}
