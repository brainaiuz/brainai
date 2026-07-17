package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 10/10/12
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
public class ConsolidationCompanySaveItem implements IsSerializable {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String companyName;
    private Integer countryId;
    private String host;
    private SelectItem baseCurrency;
    private SettingsData settingsData;
    private ArrayList<SelectItem> operatingCurrencies;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public SelectItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(SelectItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public SettingsData getSettingsData() {
        return settingsData;
    }

    public void setSettingsData(SettingsData settingsData) {
        this.settingsData = settingsData;
    }

    public ArrayList<SelectItem> getOperatingCurrencies() {
        return operatingCurrencies;
    }

    public void setOperatingCurrencies(ArrayList<SelectItem> operatingCurrencies) {
        this.operatingCurrencies = operatingCurrencies;
    }
}
