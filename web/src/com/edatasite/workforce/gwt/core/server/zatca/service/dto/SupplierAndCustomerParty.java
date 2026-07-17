package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

public class SupplierAndCustomerParty {
    private String identificationID;
    private PostalAddressItem postalAddressItem;
    private String countryIdentificationCode;
    private String companyID;
    private String registrationName;

    public String getIdentificationID() {
        return identificationID;
    }

    public void setIdentificationID(String identificationID) {
        this.identificationID = identificationID;
    }

    public PostalAddressItem getPostalAddressItem() {
        return postalAddressItem;
    }

    public void setPostalAddressItem(PostalAddressItem postalAddressItem) {
        this.postalAddressItem = postalAddressItem;
    }

    public String getCountryIdentificationCode() {
        return countryIdentificationCode;
    }

    public void setCountryIdentificationCode(String countryIdentificationCode) {
        this.countryIdentificationCode = countryIdentificationCode;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getRegistrationName() {
        return registrationName;
    }

    public void setRegistrationName(String registrationName) {
        this.registrationName = registrationName;
    }
}
