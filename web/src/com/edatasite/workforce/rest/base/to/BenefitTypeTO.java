package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 5/13/15 8:02 PM
 */
public class BenefitTypeTO implements IsSerializable {
    Integer id;
    String name;
    String description;
    SelectItemTO cashType;
    SelectItemTO quantityType;
    CurrencyTO currency;
    Double allowance;
    Boolean transferable;
    Long expirationDate;
    SelectItemTO debitAccount;
    SelectItemTO creditAccount;

    public BenefitTypeTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectItemTO getCashType() {
        return cashType;
    }

    public void setCashType(SelectItemTO cashType) {
        this.cashType = cashType;
    }

    public SelectItemTO getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(SelectItemTO quantityType) {
        this.quantityType = quantityType;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public Double getAllowance() {
        return allowance;
    }

    public void setAllowance(Double allowance) {
        this.allowance = allowance;
    }

    public Boolean getTransferable() {
        return transferable;
    }

    public void setTransferable(Boolean transferable) {
        this.transferable = transferable;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public SelectItemTO getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(SelectItemTO debitAccount) {
        this.debitAccount = debitAccount;
    }

    public SelectItemTO getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(SelectItemTO creditAccount) {
        this.creditAccount = creditAccount;
    }
}
