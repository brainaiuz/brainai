package com.edatasite.workforce.rest.v3.release10.payroll.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipItemDto {
    private ItemDto payments;
    private String type;
    private String remarks;
    private BigDecimal amount;

    public PayslipItemDto() {
    }

    public PayslipItemDto(ItemDto payments, String type, String remarks, BigDecimal amount) {
        this.payments = payments;
        this.type = type;
        this.remarks = remarks;
        this.amount = amount;
    }

    public ItemDto getPayments() {
        return payments;
    }

    public void setPayments(ItemDto payments) {
        this.payments = payments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PayslipItemDto{" +
                "payments=" + payments +
                ", type='" + type + '\'' +
                ", remarks='" + remarks + '\'' +
                ", amount=" + amount +
                '}';
    }
}
