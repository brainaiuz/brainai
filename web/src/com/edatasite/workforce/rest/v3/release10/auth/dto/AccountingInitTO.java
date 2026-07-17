package com.edatasite.workforce.rest.v3.release10.auth.dto;

import java.util.List;

public class AccountingInitTO {
    private Integer conversionDateMonthStr;
    private String conversionDateYearStr;
    private String currencyCode;
    private Integer industryId;
    private String accountingTool;
    private String taxRegistered;
    private String taxIdNumber;
    private String tinName;
    private String tinNumber;
    private String taxDisplayName;
    private String enableContractOutside;
    private String taxGenerationDate;
    private String vatRegisteredOn;
    private List<String> taxNames;
    private List<String> taxPercents;
    private String address1;
    private String address2;
    private String city;
    private Integer countryId;
    private Integer stateId;
    private String zipCode;
    private List<String> modules;
    private String paymentGateway;
    private String payPalMerchant;

    public Integer getConversionDateMonthStr() {
        return conversionDateMonthStr;
    }

    public void setConversionDateMonthStr(Integer conversionDateMonthStr) {
        this.conversionDateMonthStr = conversionDateMonthStr;
    }

    public String getConversionDateYearStr() {
        return conversionDateYearStr;
    }

    public void setConversionDateYearStr(String conversionDateYearStr) {
        this.conversionDateYearStr = conversionDateYearStr;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public String getAccountingTool() {
        return accountingTool;
    }

    public void setAccountingTool(String accountingTool) {
        this.accountingTool = accountingTool;
    }

    public String getTaxRegistered() {
        return taxRegistered;
    }

    public void setTaxRegistered(String taxRegistered) {
        this.taxRegistered = taxRegistered;
    }

    public String getTaxIdNumber() {
        return taxIdNumber;
    }

    public void setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
    }

    public String getTinName() {
        return tinName;
    }

    public void setTinName(String tinName) {
        this.tinName = tinName;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getTaxDisplayName() {
        return taxDisplayName;
    }

    public void setTaxDisplayName(String taxDisplayName) {
        this.taxDisplayName = taxDisplayName;
    }

    public String getEnableContractOutside() {
        return enableContractOutside;
    }

    public void setEnableContractOutside(String enableContractOutside) {
        this.enableContractOutside = enableContractOutside;
    }

    public String getTaxGenerationDate() {
        return taxGenerationDate;
    }

    public void setTaxGenerationDate(String taxGenerationDate) {
        this.taxGenerationDate = taxGenerationDate;
    }

    public String getVatRegisteredOn() {
        return vatRegisteredOn;
    }

    public void setVatRegisteredOn(String vatRegisteredOn) {
        this.vatRegisteredOn = vatRegisteredOn;
    }

    public List<String> getTaxNames() {
        return taxNames;
    }

    public void setTaxNames(List<String> taxNames) {
        this.taxNames = taxNames;
    }

    public List<String> getTaxPercents() {
        return taxPercents;
    }

    public void setTaxPercents(List<String> taxPercents) {
        this.taxPercents = taxPercents;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getPayPalMerchant() {
        return payPalMerchant;
    }

    public void setPayPalMerchant(String payPalMerchant) {
        this.payPalMerchant = payPalMerchant;
    }
}
