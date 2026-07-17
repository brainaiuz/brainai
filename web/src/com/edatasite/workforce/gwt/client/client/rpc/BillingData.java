package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

public class BillingData implements IsSerializable {

    private Integer clientID;
    private String address;
    private String address2;
    private String city;
    private String zipCode;
    private SelectItem country;
    private SelectItem state;
    private SelectItem[] countries;
    private SelectItem[] states;
    private Integer currencyId;
    private String processedTemplate;

    private boolean nullInstance;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public SelectItem getCountry() {
        return country;
    }

    public void setCountry(SelectItem country) {
        this.country = country;
    }

    public SelectItem getState() {
        return state;
    }

    public void setState(SelectItem state) {
        this.state = state;
    }

    public SelectItem[] getCountries() {
        return countries;
    }

    public void setCountries(SelectItem[] countries) {
        this.countries = countries;
    }

    public SelectItem[] getStates() {
        return states;
    }

    public void setStates(SelectItem[] states) {
        this.states = states;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public boolean isNullInstance() {
        return nullInstance;
    }

    public void setNullInstance(boolean nullInstance) {
        this.nullInstance = nullInstance;
    }

    public String getProcessedTemplate() {
        return processedTemplate;
    }

    public void setProcessedTemplate(String processedTemplate) {
        this.processedTemplate = processedTemplate;
    }

    public String getBillingData() {
        if(processedTemplate!=null && !"".equals(processedTemplate.trim())){
            return processedTemplate;
        }else{
            if (nullInstance) {
                return Constants.NO_BILLING_ADDRESS;
            }
            String countryName = (country != null ? country.getName() + "\n" : "");
            String stateName = ((state != null && state.getName() != null) ? state.getName() + "\n" : "");
            return address + "\n" + city + "\n" + stateName + countryName + zipCode;
        }
    }
}
