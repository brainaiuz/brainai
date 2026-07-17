package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: SherzodMuratov
 * Date: 26.01.2009
 * Time: 10:38:38
 */
public class CredentialsItem implements IsSerializable {
    private String currentPass;
    private String newPass;
    private String email;
    private String login;
    private String internationalization;//Per user stuff.
    private Integer timeZoneId;
    private SelectItem[] country;
    private Integer countryID;
    private String startPage;
    private SelectItem[] startPageLists;
    private RegistrationTypeEnum registrationType;
    private Boolean advancedPasswordEnabled;

    public String getCurrentPass() {
        return currentPass;
    }

    public void setCurrentPass(String currentPass) {
        this.currentPass = currentPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getInternationalization() {
        return internationalization;
    }

    public void setInternationalization(String internationalization) {
        this.internationalization = internationalization;
    }

    public Integer getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(Integer timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public SelectItem[] getCountry() {
        return country;
    }

    public void setCountry(SelectItem[] country) {
        this.country = country;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public SelectItem[] getStartPageLists() {
        return startPageLists;
    }

    public void setStartPageLists(SelectItem[] startPageLists) {
        this.startPageLists = startPageLists;
    }

    public void setRegistrationType(RegistrationTypeEnum registrationType) {
        this.registrationType = registrationType;
    }

    public RegistrationTypeEnum getRegistrationType() {
        return registrationType;
    }

    public Boolean getAdvancedPasswordEnabled() {
        return advancedPasswordEnabled;
    }

    public void setAdvancedPasswordEnabled(Boolean advancedPasswordEnabled) {
        this.advancedPasswordEnabled = advancedPasswordEnabled;
    }
}
