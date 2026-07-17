package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result;

public class CancelTransactionResult {
    private String transaction;
    private Long cancel_time;
    private Integer state;

    public CancelTransactionResult(String transaction, Long cancel_time, Integer state) {
        this.transaction = transaction;
        this.cancel_time = cancel_time;
        this.state = state;
    }

    public String getTransaction() {
        return transaction;
    }

    public Long getCancel_time() {
        return cancel_time;
    }

    public Integer getState() {
        return state;
    }
}
