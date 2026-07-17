package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

/**
 * User : Dilsh0d Madrahimov on 9/16/2019 11:27 PM
 */
public class FinancialInformationAddDTO extends ResponseData {
    private Integer currency_id;
    private BigDecimal opening_balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private String as_of_date;
    private String registration_number;
    private String vat_number;
    private Integer tax_id;
    private Integer terms_id;
    private Integer payment_method_id;
    private Integer bank_account_id;

    public Integer getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(Integer currency_id) {
        this.currency_id = currency_id;
    }

    public BigDecimal getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(BigDecimal opening_balance) {
        this.opening_balance = opening_balance;
    }

    public String getAs_of_date() {
        return as_of_date;
    }

    public void setAs_of_date(String as_of_date) {
        this.as_of_date = as_of_date;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getVat_number() {
        return vat_number;
    }

    public void setVat_number(String vat_number) {
        this.vat_number = vat_number;
    }

    public Integer getTax_id() {
        return tax_id;
    }

    public void setTax_id(Integer tax_id) {
        this.tax_id = tax_id;
    }

    public Integer getTerms_id() {
        return terms_id;
    }

    public void setTerms_id(Integer terms_id) {
        this.terms_id = terms_id;
    }

    public Integer getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(Integer payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public Integer getBank_account_id() {
        return bank_account_id;
    }

    public void setBank_account_id(Integer bank_account_id) {
        this.bank_account_id = bank_account_id;
    }
}
