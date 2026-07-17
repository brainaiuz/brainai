package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import java.util.HashMap;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 14-Oct-2010
 * Time: 20:15:05
 */
public class ITextCompanyData {

    private String companyName;
    private String address;
    private String address2;
    private String city;
    private String postCode;
    private String country;
    private String state;
    private String cityPostCode;
    private String companyPhone;
    private String companyFax;
    private String companyEmail;
    private String companyLogoUrl;
    private String approveStampUrl;
    private String paidStampUrl;
    private String receivedStampUrl;
    private String overdueStampUrl;
    private String website;
    private String baseCurrency;
    private String buildingNumber;
    private String plotIdentification;
    private HashMap<String, CustomisedITextTable> customData;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCityPostCode() {
        return cityPostCode;
    }

    public void setCityPostCode(String cityPostCode) {
        this.cityPostCode = cityPostCode;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyFax() {
        return companyFax;
    }

    public void setCompanyFax(String companyFax) {
        this.companyFax = companyFax;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public String getApproveStampUrl() {
        return approveStampUrl;
    }

    public void setApproveStampUrl(String approveStampUrl) {
        this.approveStampUrl = approveStampUrl;
    }

    public String getPaidStampUrl() {
        return paidStampUrl;
    }

    public void setPaidStampUrl(String paidStampUrl) {
        this.paidStampUrl = paidStampUrl;
    }

    public String getReceivedStampUrl() {
        return receivedStampUrl;
    }

    public void setReceivedStampUrl(String receivedStampUrl) {
        this.receivedStampUrl = receivedStampUrl;
    }

    public String getOverdueStampUrl() {
        return overdueStampUrl;
    }

    public void setOverdueStampUrl(String overdueStampUrl) {
        this.overdueStampUrl = overdueStampUrl;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public HashMap<String, CustomisedITextTable> getCustomData() {
        return customData;
    }

    public void setCustomData(HashMap<String, CustomisedITextTable> customData) {
        this.customData = customData;
    }

    public String getBaseCurrency(){
        return this.baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency){
        this.baseCurrency = baseCurrency;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getPlotIdentification() {
        return plotIdentification;
    }

    public void setPlotIdentification(String plotIdentification) {
        this.plotIdentification = plotIdentification;
    }
}
