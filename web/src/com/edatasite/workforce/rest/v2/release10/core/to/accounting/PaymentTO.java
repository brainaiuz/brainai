package com.edatasite.workforce.rest.v2.release10.core.to.accounting;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Author: Azazello
 * Date: 2/13/2018
 * Time: 3:19 PM
 */
public class PaymentTO extends ResponseData {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Schema(required = true)
    private String paid_date;
    private BigDecimal paid_amount;
    private Integer payment_account_id;
    private Integer payment_method_id;
    private String reference;

    public String getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(String paid_date) {
        this.paid_date = paid_date;
    }

    public BigDecimal getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(BigDecimal paid_amount) {
        this.paid_amount = paid_amount;
    }

    public Integer getPayment_account_id() {
        return payment_account_id;
    }

    public void setPayment_account_id(Integer payment_account_id) {
        this.payment_account_id = payment_account_id;
    }

    public Integer getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(Integer payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
