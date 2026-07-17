package com.edatasite.workforce.rest.v3.release10.core.request;

import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Normurod Buriev.
 * Date: 1/5/2021 6:48 PM
 */
public class BaseCrmAccountRequest implements Serializable {
    private Integer id;
    @JsonAlias({"objectkey", "objectKey"})
    private String objectKey;
    @NotBlank(message = "Name field cannot bn empty.")
    @NotNull(message = "Name is required.")
    private String name;
    private String number;
    @Email(message = "Email is not Valid.")
    private String email;
    private String phone;
    private String website;
    private String fax;
    private String industry;
    private List<IdCode> owners;

    private String currency;
    private IdName terms;
    private List<ItemDto> accountTypes;

    @Valid
    private List<AddressDto> billingAddresses;
    @Valid
    private List<AddressDto> shippingAddresses;
    @Valid
    private List<NoteDto> notes;
    @Valid
    private List<CustomFieldRequest> customFields;

    public BaseCrmAccountRequest() {
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public List<IdCode> getOwners() {
        return owners;
    }

    public void setOwners(List<IdCode> owners) {
        this.owners = owners;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public IdName getTerms() {
        return terms;
    }

    public void setTerms(IdName terms) {
        this.terms = terms;
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

    public List<CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public List<ItemDto> getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(List<ItemDto> accountTypes) {
        this.accountTypes = accountTypes;
    }

    @JsonIgnore
    public boolean isExistingObject() {
        return StringUtils.isNotBlank(getObjectKey()) || getId() != null || StringUtils.isNotBlank(getNumber());
    }

}
