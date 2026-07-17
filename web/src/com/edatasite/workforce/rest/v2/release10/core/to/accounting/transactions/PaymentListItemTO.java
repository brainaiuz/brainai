package com.edatasite.workforce.rest.v2.release10.core.to.accounting.transactions;

import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Author: Anvar Akramov
 * Date: 2/13/2018
 * Time: 3:19 PM
 */
public class PaymentListItemTO extends ResponseData {

    Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date paid_date;
    private BigDecimal paid_amount;
    private IdNameTO account;
    private Integer payment_method_id;
    private String reference;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(Date paid_date) {
        this.paid_date = paid_date;
    }

    public BigDecimal getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(BigDecimal paid_amount) {
        this.paid_amount = paid_amount;
    }

    public IdNameTO getAccount() {
        return account;
    }

    public void setAccount(IdNameTO account) {
        this.account = account;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
