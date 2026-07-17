package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 4/6/15 12:54 PM
 */
public class CrmAccountTO implements IsSerializable {
    Integer id;
    String name;
    String number;
    String phoneNumber;
    String email;
    String website;
    String fax;
    SelectItemTO accountOwner;
    SelectItemTO parentAccount;
    SelectItemTO subsidiary;
    SelectItemTO clientType;
    SelectItemTO vatCategory;

    SelectItemTO primaryContact;
    Double supplierBalance;
    Double customerBalance;
    SelectItemTO country;
    SelectItemTO currency;
    SelectItemTO industry;
    SelectItemTO organizationType;
    SelectItemTO annualRevenue;
    SelectItemTO numberOfEmployees;
    SelectItemTO ownership;
    SelectItemTO owner;
    SelectItemTO rating;
    SelectItemTO paymentMethod;
    List<CheckListItemTO> accountTypes;
    List<CheckListItemTO> priceLevel;
    AddressTO billingAddress;
    AddressTO mailingAddress;
    Long asOfDate;
    SelectItemTO bankAccount;
    SelectItemTO accountReceivable;
    SelectItemTO accountPayable;
    String vatNumber;
    BigDecimal creditLimit;
    String registrationNumber;
    SelectItemTO tax;
    SelectItemTO terms;


    public CrmAccountTO() {
    }

    public CrmAccountTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public CrmAccountTO(CrmAccountItem item, boolean brief) {
        this.id = item.getObjectId();
        this.name = item.getName();
        this.number = item.getNumber();
        if (!brief) {
            this.country = new SelectItemTO(item.getDefaultAddress(true).getCountryId(), item.getDefaultAddress(true).getCountry());
            this.currency = new SelectItemTO(item.getCurrencyId(), item.getCurrency());
            this.phoneNumber = item.getPhone();
            this.email = item.getEmail();
            this.website = item.getWebsite();
            this.fax = item.getFax();
            this.customerBalance = item.getBalanceAmount();
            this.supplierBalance = item.getSupplierBalanceAmount();
            this.primaryContact = item.getPrimaryContact() == null ? null : new SelectItemTO(item.getPrimaryContact().getObjectId(), item.getPrimaryContact().getName());
            this.accountOwner = null;//item.getOwnerID() == null ? null : new SelectItemTO(item.getOwnerID(), item.getOwnerName());
            this.parentAccount = item.getParent() == null ? null : new SelectItemTO(item.getParent().getObjectId(), item.getParent().getName());
            this.subsidiary = WrapUtils.wrapSelectItemTO(item.getSubsidiary());
            this.accountTypes = WrapUtils.wrapCheckListItemTOs(item.getAccountTypes());
            this.priceLevel = WrapUtils.wrapCheckListItemTOs(item.getAppliedPriceLavel());
            this.clientType = WrapUtils.wrapSelectItemTO(item.getClientType());
            this.vatCategory = WrapUtils.wrapSelectItemTO(item.getVatCategory());
            this.industry = item.getIndustryID() == null ? null : new SelectItemTO(item.getIndustryID(), item.getIndustry(), item.getIndustryCode(), "");
            this.owner = null;//item.getOwnerID() == null ? null : new SelectItemTO(item.getOwnerID(), item.getOwnerName());
            this.paymentMethod = item.getPaymentMethodId() == null ? null : new SelectItemTO(item.getPaymentMethodId(), item.getPaymentMethod());
            this.billingAddress = new AddressTO(item.getDefaultAddress(true));
            this.mailingAddress = new AddressTO(item.getDefaultAddress(false));
            if (item.getBalanceDate() != null && item.getBalanceDate().getDate() != null) {
                this.asOfDate = item.getBalanceDate().getDate().getTime();
            }
            if (item.getSupplierBalanceDate() != null && item.getSupplierBalanceDate().getDate() != null) {
                this.asOfDate = item.getSupplierBalanceDate().getDate().getTime();
            }
            this.bankAccount = item.getBankAccountId() == null ? null : new SelectItemTO(item.getBankAccountId(), item.getBankAccount());
            this.accountReceivable = WrapUtils.wrapSelectItemTO(item.getAccountsReceivablePayable());
            this.accountPayable = WrapUtils.wrapSelectItemTO(item.getAccountsReceivablePayable());
            this.vatNumber = item.getVatNumber();
            this.creditLimit = item.getCreditLimit();
            this.registrationNumber = item.getRegistrationNumber();
            this.tax = WrapUtils.wrapSelectItemTO(item.getVat());
            this.terms = WrapUtils.wrapSelectItemTO(item.getTermsItem());
        }
    }

    public CrmAccountItem wrap(CrmAccountTO crmAccountTO) {
        CrmAccountItem item = new CrmAccountItem();
        item.setObjectId(crmAccountTO.getId());
        item.setNumber(crmAccountTO.getNumber());
        item.setName(crmAccountTO.getName());
        /*if (crmAccountTO.getOwner() != null) {
            item.setOwnerID(crmAccountTO.getOwner().getId());
        }*/
        if (crmAccountTO.getCurrency() != null) {
            item.setCurrencyId(crmAccountTO.getCurrency().getId());
            item.setCurrency(crmAccountTO.getCurrency().getName());
        }
        if (crmAccountTO.getPhoneNumber() != null) {
            item.setPhone(crmAccountTO.getPhoneNumber());
        }
        if (crmAccountTO.getEmail() != null) {
            item.setEmail(crmAccountTO.getEmail());
        }
        if (crmAccountTO.getWebsite() != null) {
            item.setWebsite(crmAccountTO.getWebsite());
        }
        if (crmAccountTO.getFax() != null) {
            item.setFax(crmAccountTO.getFax());
        }
        if (crmAccountTO.getCustomerBalance() != null) {
            item.setBalanceAmount(crmAccountTO.getCustomerBalance());
        }
        if (crmAccountTO.getSupplierBalance() != null) {
            item.setSupplierBalanceAmount(crmAccountTO.getSupplierBalance());
        }
        if (crmAccountTO.getAsOfDate() != null) {
            item.setBalanceDate(new DateNonConvertable(WrapUtils.longToDate(crmAccountTO.getAsOfDate())));
            item.setSupplierBalanceDate(new DateNonConvertable(WrapUtils.longToDate(crmAccountTO.getAsOfDate())));
        }

        if (crmAccountTO.getPrimaryContact() != null) {
            ContactListItem primaryContact = new ContactListItem();
            primaryContact.setObjectId(crmAccountTO.getPrimaryContact().getId());
            primaryContact.setContactName(crmAccountTO.getPrimaryContact().getName());
            item.setPrimaryContact(primaryContact);
        }
        /*if (crmAccountTO.getAccountOwner() != null) {
            item.setOwnerID(crmAccountTO.getAccountOwner().getId());
            item.setOwnerName(crmAccountTO.getAccountOwner().getName());
        }*/
        if (crmAccountTO.getParentAccount() != null) {
            CrmAccountItem parentAccount = new CrmAccountItem();
            parentAccount.setObjectId(crmAccountTO.getAccountOwner().getId());
            parentAccount.setName(crmAccountTO.getAccountOwner().getName());
            item.setParent(parentAccount);
        }
        if (crmAccountTO.getSubsidiary() != null) {
            item.setSubsidiary(WrapUtils.wrapSelectItem(crmAccountTO.getSubsidiary()));
        }
        if (crmAccountTO.getAccountTypes() != null) {
            List<CheckListItemTO> accountTypeTOs = crmAccountTO.getAccountTypes();
            if (accountTypeTOs != null && !accountTypeTOs.isEmpty()) {
                List<SelectItem> accountTypes = new ArrayList<>(accountTypeTOs.size());
                for (CheckListItemTO accountType : accountTypeTOs) {
                    accountTypes.add(new SelectItem(accountType.getId(), accountType.getName(), accountType.getDescription(), accountType.getSelected()));
                }
                item.setAccountTypes(accountTypes.toArray(new SelectItem[0]));
            }
        }
        if (crmAccountTO.getPriceLevel() != null) {
            List<CheckListItemTO> priceLevelTOs = crmAccountTO.getPriceLevel();
            if (priceLevelTOs != null && !priceLevelTOs.isEmpty()) {
                List<SelectItem> priceLevels = new ArrayList<>(priceLevelTOs.size());
                for (CheckListItemTO priveLevel : priceLevelTOs) {
                    priceLevels.add(new SelectItem(priveLevel.getId(), priveLevel.getName(), priveLevel.getDescription(), priveLevel.getSelected()));
                }
                item.setAppliedPriceLavel(priceLevels.toArray(new SelectItem[0]));
            }
        }
        if (crmAccountTO.getClientType() != null) {
            item.setClientType(WrapUtils.wrapSelectItem(crmAccountTO.getClientType()));
        }
        if (crmAccountTO.getVatCategory() != null) {
            item.setVatCategory(WrapUtils.wrapSelectItem(crmAccountTO.getVatCategory()));
        }
        if (crmAccountTO.getIndustry() != null) {
            item.setIndustryID(crmAccountTO.getIndustry().getId());
            item.setIndustry(crmAccountTO.getIndustry().getName());
            item.setIndustryCode(crmAccountTO.getIndustry().getCode());
        }
        /*if (crmAccountTO.getOwner() != null) {
            item.setOwnerID(crmAccountTO.getOwner().getId());
            item.setOwnerName(crmAccountTO.getOwner().getName());
        }*/
        if (crmAccountTO.getPaymentMethod() != null) {
            item.setPaymentMethodId(crmAccountTO.getPaymentMethod().getId());
            item.setPaymentMethod(crmAccountTO.getPaymentMethod().getName());
        }
        if (crmAccountTO.getAsOfDate() != null) {
            item.setBalanceDate(new DateNonConvertable(WrapUtils.longToDate(crmAccountTO.getAsOfDate())));
        }
        if (crmAccountTO.getBankAccount() != null) {
            item.setBankAccountId(crmAccountTO.getBankAccount().getId());
            item.setBankAccount(crmAccountTO.getBankAccount().getName());
        }
        if (crmAccountTO.getAccountPayable() != null) {
            item.setAccountsReceivablePayable(new AccountItem(crmAccountTO.getAccountPayable().getId(), crmAccountTO.getAccountPayable().getName()));
        }
        if (crmAccountTO.getAccountReceivable() != null) {
            item.setAccountsReceivablePayable(new AccountItem(crmAccountTO.getAccountReceivable().getId(), crmAccountTO.getAccountReceivable().getName()));
        }
        if (crmAccountTO.getVatNumber() != null) {
            item.setVatNumber(crmAccountTO.getVatNumber());
        }
        if (crmAccountTO.getCreditLimit() != null) {
            item.setCreditLimit(crmAccountTO.getCreditLimit());
        }
        if (crmAccountTO.getRegistrationNumber() != null) {
            item.setRegistrationNumber(crmAccountTO.getRegistrationNumber());
        }
        if (crmAccountTO.getTax() != null) {
            item.setTaxName(crmAccountTO.getTax().getName());
        }
        if (crmAccountTO.getTerms() != null) {
            item.setTermsItem(WrapUtils.wrapSelectItem(crmAccountTO.getTerms()));
        }
        if (crmAccountTO.getBillingAddress() != null) {
            Address address = new Address();
            address.setPrimary(crmAccountTO.getBillingAddress().getIsPrimary());
            address.setName(crmAccountTO.getBillingAddress().getName());
            address.setAddress(crmAccountTO.getBillingAddress().getAddress1());
            address.setAddressb(crmAccountTO.getBillingAddress().getAddress2());
            address.setCity(crmAccountTO.getBillingAddress().getCity());
            if (crmAccountTO.getBillingAddress().getCountry() != null) {
                address.setCountryId(crmAccountTO.getBillingAddress().getCountry().getId());
                address.setCountry(crmAccountTO.getBillingAddress().getCountry().getName());
                address.setCountryCode(crmAccountTO.getBillingAddress().getCountry().getCode());
            }
            if (crmAccountTO.getBillingAddress().getState() != null) {
                address.setStateId(crmAccountTO.getBillingAddress().getState().getId());
                address.setState(crmAccountTO.getBillingAddress().getState().getName());
            }
            address.setEntityID(crmAccountTO.getId());
            address.setEntityType(EdsAddress.ENTITY_TYPE_COMPANY);
            address.setRelationType(crmAccountTO.getBillingAddress().getType() != null ? crmAccountTO.getBillingAddress().getType().getId() : EdsAddress.HOME);
            item.setBillAddresses(new Address[]{address});
        }
        if (crmAccountTO.getMailingAddress() != null) {
            Address address = new Address();
            address.setPrimary(crmAccountTO.getMailingAddress().getIsPrimary());
            address.setName(crmAccountTO.getMailingAddress().getName());
            address.setAddress(crmAccountTO.getMailingAddress().getAddress1());
            address.setAddressb(crmAccountTO.getMailingAddress().getAddress2());
            address.setCity(crmAccountTO.getMailingAddress().getCity());
            if (crmAccountTO.getMailingAddress().getCountry() != null) {
                address.setCountryId(crmAccountTO.getMailingAddress().getCountry().getId());
                address.setCountry(crmAccountTO.getMailingAddress().getCountry().getName());
                address.setCountryCode(crmAccountTO.getMailingAddress().getCountry().getCode());
            }
            if (crmAccountTO.getMailingAddress().getState() != null) {
                address.setStateId(crmAccountTO.getMailingAddress().getState().getId());
                address.setState(crmAccountTO.getMailingAddress().getState().getName());
            }
            address.setEntityID(crmAccountTO.getId());
            address.setEntityType(EdsAddress.ENTITY_TYPE_COMPANY);
            address.setRelationType(crmAccountTO.getMailingAddress().getType() != null ? crmAccountTO.getMailingAddress().getType().getId() : EdsAddress.HOME);
            item.setMailAddresses(new Address[]{address});
        }

        return item;
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

    public SelectItemTO getIndustry() {
        return industry;
    }

    public void setIndustry(SelectItemTO industry) {
        this.industry = industry;
    }

    public SelectItemTO getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(SelectItemTO organizationType) {
        this.organizationType = organizationType;
    }

    public SelectItemTO getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(SelectItemTO annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public SelectItemTO getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(SelectItemTO numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public SelectItemTO getOwnership() {
        return ownership;
    }

    public void setOwnership(SelectItemTO ownership) {
        this.ownership = ownership;
    }

    public List<CheckListItemTO> getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(List<CheckListItemTO> accountTypes) {
        this.accountTypes = accountTypes;
    }

    public List<CheckListItemTO> getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(List<CheckListItemTO> priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public SelectItemTO getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(SelectItemTO accountOwner) {
        this.accountOwner = accountOwner;
    }

    public SelectItemTO getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(SelectItemTO primaryContact) {
        this.primaryContact = primaryContact;
    }


    public SelectItemTO getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItemTO currency) {
        this.currency = currency;
    }

    public Double getSupplierBalance() {
        return supplierBalance;
    }

    public void setSupplierBalance(Double supplierBalance) {
        this.supplierBalance = supplierBalance;
    }

    public Double getCustomerBalance() {
        return customerBalance;
    }

    public void setCustomerBalance(Double customerBalance) {
        this.customerBalance = customerBalance;
    }

    public SelectItemTO getCountry() {
        return country;
    }

    public void setCountry(SelectItemTO country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public SelectItemTO getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(SelectItemTO parentAccount) {
        this.parentAccount = parentAccount;
    }

    public SelectItemTO getSubsidiary() {
        return subsidiary;
    }

    public void setSubsidiary(SelectItemTO subsidiary) {
        this.subsidiary = subsidiary;
    }

    public SelectItemTO getClientType() {
        return clientType;
    }

    public void setClientType(SelectItemTO clientType) {
        this.clientType = clientType;
    }

    public SelectItemTO getVatCategory() {
        return vatCategory;
    }

    public void setVatCategory(SelectItemTO vatCategory) {
        this.vatCategory = vatCategory;
    }

    public SelectItemTO getOwner() {
        return owner;
    }

    public void setOwner(SelectItemTO owner) {
        this.owner = owner;
    }

    public SelectItemTO getRating() {
        return rating;
    }

    public void setRating(SelectItemTO rating) {
        this.rating = rating;
    }

    public SelectItemTO getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(SelectItemTO paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public AddressTO getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(AddressTO billingAddress) {
        this.billingAddress = billingAddress;
    }

    public AddressTO getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(AddressTO mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public Long getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Long asOfDate) {
        this.asOfDate = asOfDate;
    }

    public SelectItemTO getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(SelectItemTO bankAccount) {
        this.bankAccount = bankAccount;
    }

    public SelectItemTO getAccountReceivable() {
        return accountReceivable;
    }

    public void setAccountReceivable(SelectItemTO accountReceivable) {
        this.accountReceivable = accountReceivable;
    }

    public SelectItemTO getAccountPayable() {
        return accountPayable;
    }

    public void setAccountPayable(SelectItemTO accountPayable) {
        this.accountPayable = accountPayable;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public SelectItemTO getTax() {
        return tax;
    }

    public void setTax(SelectItemTO tax) {
        this.tax = tax;
    }

    public SelectItemTO getTerms() {
        return terms;
    }

    public void setTerms(SelectItemTO terms) {
        this.terms = terms;
    }
}
