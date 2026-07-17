package com.edatasite.workforce.rest.v3.release10.accounting.request;

import com.edatasite.workforce.rest.v3.release10.accounting.dto.BankDetailsDto;
import com.edatasite.workforce.rest.v3.release10.core.request.BaseCrmAccountRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Normurod Buriev.
 * Date: 1/5/2021 8:08 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerSupplierRequest extends BaseCrmAccountRequest {
    private String vatNumber;
    private IdName tax;
    private IdName paymentMethod;
    private String bankAccount;
    private BigDecimal balance;
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date balanceAsOfDate;
    private BigDecimal creditLimit;
    private BankDetailsDto bankDetails;

    public CustomerSupplierRequest() {
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public IdName getTax() {
        return tax;
    }

    public void setTax(IdName tax) {
        this.tax = tax;
    }

    public IdName getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(IdName paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getBalanceAsOfDate() {
        return balanceAsOfDate;
    }

    public void setBalanceAsOfDate(Date balanceAsOfDate) {
        this.balanceAsOfDate = balanceAsOfDate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BankDetailsDto getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetailsDto bankDetails) {
        this.bankDetails = bankDetails;
    }
}
