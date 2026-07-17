package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result;

public class CheckTransactionResult {
    private Long create_time;
    private Long perform_time;
    private Long cancel_time;
    private String transaction;
    private Integer state;
    private Integer reason;

    public CheckTransactionResult(Long create_time, Long perform_time, Long cancel_time, String transaction, Integer state, Integer reason) {
        this.create_time = create_time;
        this.perform_time = perform_time;
        this.cancel_time = cancel_time;
        this.transaction = transaction;
        this.state = state;
        this.reason = reason;
    }

    public CheckTransactionResult() {

    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Long getPerform_time() {
        return perform_time;
    }

    public void setPerform_time(Long perform_time) {
        this.perform_time = perform_time;
    }

    public Long getCancel_time() {
        return cancel_time;
    }

    public void setCancel_time(Long cancel_time) {
        this.cancel_time = cancel_time;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }
}
