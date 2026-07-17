package com.edatasite.workforce.rest.v3.release10.crm.dto;

import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;

public class OpportunityContactTO {
    private SelectItem[] emails;
    private SelectItem[] phoneNumbers;
    private SelectItem[] address;
    private SelectItem[] imAddress;
    private SelectItem[] webSites;
    private SelectItem[] countries;
    private SelectItem[] cities;
    private SelectItem[] states;
    private SelectItem[] postCods;

    private String primaryEmail;  //need for getting contact's single email address
    private Address primaryAddress;  //need for getting contact's single address
    private String primaryPhone;  //need for getting contact's single phone


    private DateNonConvertable birthDate;

    private ArrayList<Address> addresses = new ArrayList<>();

    private ArrayList<SelectItem> telegramChats;


    public SelectItem[] getEmails() {
        return emails;
    }

    public void setEmails(SelectItem[] emails) {
        this.emails = emails;
    }

    public SelectItem[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(SelectItem[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public SelectItem[] getAddress() {
        return address;
    }

    public void setAddress(SelectItem[] address) {
        this.address = address;
    }

    public SelectItem[] getImAddress() {
        return imAddress;
    }

    public void setImAddress(SelectItem[] imAddress) {
        this.imAddress = imAddress;
    }

    public SelectItem[] getWebSites() {
        return webSites;
    }

    public void setWebSites(SelectItem[] webSites) {
        this.webSites = webSites;
    }

    public SelectItem[] getCountries() {
        return countries;
    }

    public void setCountries(SelectItem[] countries) {
        this.countries = countries;
    }

    public SelectItem[] getCities() {
        return cities;
    }

    public void setCities(SelectItem[] cities) {
        this.cities = cities;
    }

    public SelectItem[] getStates() {
        return states;
    }

    public void setStates(SelectItem[] states) {
        this.states = states;
    }

    public SelectItem[] getPostCods() {
        return postCods;
    }

    public void setPostCods(SelectItem[] postCods) {
        this.postCods = postCods;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public DateNonConvertable getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(DateNonConvertable birthDate) {
        this.birthDate = birthDate;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public ArrayList<SelectItem> getTelegramChats() {
        return telegramChats;
    }

    public void setTelegramChats(ArrayList<SelectItem> telegramChats) {
        this.telegramChats = telegramChats;
    }
}
