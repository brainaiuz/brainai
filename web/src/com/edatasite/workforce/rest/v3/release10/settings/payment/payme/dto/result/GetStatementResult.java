package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result;

import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.PaymeAccount;
import com.google.gson.annotations.SerializedName;

public class GetStatementResult {
    private String id;
    private Long time;
    private Long amount;
    private PaymeAccount account;
    @SerializedName("create_time")
    private Long createTime;
    @SerializedName("perform_time")
    private Long performTime;
    @SerializedName("cancel_time")
    private Long cancelTime;
    private String transaction;
    private Integer state;
    private Integer reason;

    public GetStatementResult(String id, Long time, Long amount, PaymeAccount account, Long createTime, Long performTime, Long cancelTime, String transaction, Integer state, Integer reason) {
        this.id = id;
        this.time = time;
        this.amount = amount;
        this.account = account;
        this.createTime = createTime;
        this.performTime = performTime;
        this.cancelTime = cancelTime;
        this.transaction = transaction;
        this.state = state;
        this.reason = reason;
    }

    public GetStatementResult() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setAccount(PaymeAccount account) {
        this.account = account;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setPerformTime(Long performTime) {
        this.performTime = performTime;
    }

    public void setCancelTime(Long cancelTime) {
        this.cancelTime = cancelTime;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
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

    public Long getCreateTime() {
        return createTime;
    }

    public Long getPerformTime() {
        return performTime;
    }

    public Long getCancelTime() {
        return cancelTime;
    }

    public String getTransaction() {
        return transaction;
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
