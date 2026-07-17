package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result;

import com.google.gson.annotations.SerializedName;

public class CreateTransactionResult {
    @SerializedName("create_time")
    private Long create_time;
    private String transaction;
    private Integer state;

    public CreateTransactionResult(Long create_time, String transaction, Integer state) {
        this.create_time = create_time;
        this.transaction = transaction;
        this.state = state;
    }

    public CreateTransactionResult() {

    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public String getTransaction() {
        return transaction;
    }

    public Integer getState() {
        return state;
    }
}
