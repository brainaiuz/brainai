package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFieldDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Normurod Buriev.
 * Date: 1/11/2021 1:39 PM
 */
public class CustomerSupplierDto {
    private Integer id;
    private String objectKey;
    private String name;
    private String number;
    private String email;
    private String phone;
    private String website;
    private String fax;
    private ItemDto industry;
    private List<ItemDto> owners;

    private List<AddressDto> billingAddresses;
    private List<AddressDto> shippingAddresses;
    private List<NoteDto> notes;
    private List<CustomFieldDto> customFields;

    private String vatNumber;
    private String currency;
    private IdName tax;
    private IdName terms;
    private IdName paymentMethod;
    private ItemDto bankAccount;
    private BigDecimal balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date balanceAsOfDate;

    private BigDecimal creditLimit;

    private BankDetailsDto bankDetails;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDate;

    public CustomerSupplierDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public ItemDto getIndustry() {
        return industry;
    }

    public void setIndustry(ItemDto industry) {
        this.industry = industry;
    }

    public List<ItemDto> getOwners() {
        return owners;
    }

    public void setOwners(List<ItemDto> owners) {
        this.owners = owners;
    }

    public List<AddressDto> getBillingAddresses() {
        return billingAddresses;
    }

    public void setBillingAddresses(List<AddressDto> billingAddresses) {
        this.billingAddresses = billingAddresses;
    }

    public List<AddressDto> getShippingAddresses() {
        return shippingAddresses;
    }

    public void setShippingAddresses(List<AddressDto> shippingAddresses) {
        this.shippingAddresses = shippingAddresses;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDto> notes) {
        this.notes = notes;
    }

    public List<CustomFieldDto> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomFieldDto> customFields) {
        this.customFields = customFields;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public IdName getTax() {
        return tax;
    }

    public void setTax(IdName tax) {
        this.tax = tax;
    }

    public IdName getTerms() {
        return terms;
    }

    public void setTerms(IdName terms) {
        this.terms = terms;
    }

    public IdName getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(IdName paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ItemDto getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(ItemDto bankAccount) {
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
