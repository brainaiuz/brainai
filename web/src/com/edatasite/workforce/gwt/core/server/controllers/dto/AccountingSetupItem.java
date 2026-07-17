package com.edatasite.workforce.gwt.core.server.controllers.dto;

import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.core.client.rpc.Address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountingSetupItem implements Serializable {
    private Date conversionDate;
    private Integer industryId;
    private String accountingTool;
    private Address companyBillingAddress;
    private ArrayList<TaxData> taxes;
    private List<String> modules;
    private String paymentGateway;
    private String payPalMerchant;

    /**
     * Tax settings
     */
    private boolean isTaxRegistered;
    private String taxIdNumber;
    private String taxDisplayName;
    private String taxRegisterName;
    private String taxRegisterNumber;
    private boolean enableContractOutsite;
    private Date taxGenerationDate;
    private Date vatRegisteredOn;

    private String currencyCode;

    public Date getConversionDate() {
        return conversionDate;
    }

    public AccountingSetupItem setConversionDate(Date conversionDate) {
        this.conversionDate = conversionDate;
        return this;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public AccountingSetupItem setIndustryId(Integer industryId) {
        this.industryId = industryId;
        return this;
    }

    public String getAccountingTool() {
        return accountingTool;
    }

    public AccountingSetupItem setAccountingTool(String accountingTool) {
        this.accountingTool = accountingTool;
        return this;
    }

    public Address getCompanyBillingAddress() {
        return companyBillingAddress;
    }

    public AccountingSetupItem setCompanyBillingAddress(Address companyBillingAddress) {
        this.companyBillingAddress = companyBillingAddress;
        return this;
    }

    public String getTaxIdNumber() {
        return taxIdNumber;
    }

    public AccountingSetupItem setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
        return this;
    }

    public String getTaxDisplayName() {
        return taxDisplayName;
    }

    public AccountingSetupItem setTaxDisplayName(String taxDisplayName) {
        this.taxDisplayName = taxDisplayName;
        return this;
    }

    public String getTaxRegisterName() {
        return taxRegisterName;
    }

    public AccountingSetupItem setTaxRegisterName(String taxRegisterName) {
        this.taxRegisterName = taxRegisterName;
        return this;
    }

    public String getTaxRegisterNumber() {
        return taxRegisterNumber;
    }

    public AccountingSetupItem setTaxRegisterNumber(String taxRegisterNumber) {
        this.taxRegisterNumber = taxRegisterNumber;
        return this;
    }

    public ArrayList<TaxData> getTaxes() {
        return taxes;
    }

    public AccountingSetupItem setTaxes(ArrayList<TaxData> taxes) {
        this.taxes = taxes;
        return this;
    }

    public List<String> getModules() {
        return modules;
    }

    public AccountingSetupItem setModules(List<String> modules) {
        this.modules = modules;
        return this;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public AccountingSetupItem setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
        return this;
    }

    public String getPayPalMerchant() {
        return payPalMerchant;
    }

    public AccountingSetupItem setPayPalMerchant(String payPalMerchant) {
        this.payPalMerchant = payPalMerchant;
        return this;
    }

    public boolean isTaxRegistered() {
        return isTaxRegistered;
    }

    public AccountingSetupItem setTaxRegistered(boolean taxRegistered) {
        isTaxRegistered = taxRegistered;
        return this;
    }

    public boolean isEnableContractOutsite() {
        return enableContractOutsite;
    }

    public AccountingSetupItem setEnableContractOutsite(boolean enableContractOutsite) {
        this.enableContractOutsite = enableContractOutsite;
        return this;
    }

    public Date getTaxGenerationDate() {
        return taxGenerationDate;
    }

    public AccountingSetupItem setTaxGenerationDate(Date taxGenerationDate) {
        this.taxGenerationDate = taxGenerationDate;
        return this;
    }

    public Date getVatRegisteredOn() {
        return vatRegisteredOn;
    }

    public AccountingSetupItem setVatRegisteredOn(Date vatRegisteredOn) {
        this.vatRegisteredOn = vatRegisteredOn;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public AccountingSetupItem setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }
}
