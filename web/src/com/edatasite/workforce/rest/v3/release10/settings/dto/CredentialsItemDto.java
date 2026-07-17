package com.edatasite.workforce.rest.v3.release10.settings.dto;

import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;

public class CredentialsItemDto {

    private String email;
    private String login;
    private String internationalization;
    private Integer timeZoneId;
    private Integer countryID;
    private String startPage;
    private RegistrationTypeEnum registrationType;
    private Boolean advancedPasswordEnabled;

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

    public RegistrationTypeEnum getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationTypeEnum registrationType) {
        this.registrationType = registrationType;
    }

    public Boolean getAdvancedPasswordEnabled() {
        return advancedPasswordEnabled;
    }

    public void setAdvancedPasswordEnabled(Boolean advancedPasswordEnabled) {
        this.advancedPasswordEnabled = advancedPasswordEnabled;
    }
}