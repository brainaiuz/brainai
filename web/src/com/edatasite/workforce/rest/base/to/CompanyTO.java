package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.rest.base.enums.SignUpTypeEnum;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CompanyTO implements IsSerializable {

    private AuthTO authInfo;

    private Integer id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String logo;
    private String locale;
    private String host;
    private CountryTO country;
    private SignUpTypeEnum signUpType;
    private String shortDateFormat;
    private String longDateFormat;

    public CompanyTO() {
    }

    public AuthTO getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(AuthTO authInfo) {
        this.authInfo = authInfo;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

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

    public CountryTO getCountry() {
        return country;
    }

    public void setCountry(CountryTO country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public SignUpTypeEnum getSignUpType() {
        return signUpType;
    }

    public void setSignUpType(SignUpTypeEnum signUpType) {
        this.signUpType = signUpType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShortDateFormat() {
        return shortDateFormat;
    }

    public void setShortDateFormat(String shortDateFormat) {
        this.shortDateFormat = shortDateFormat;
    }

    public String getLongDateFormat() {
        return longDateFormat;
    }

    public void setLongDateFormat(String longDateFormat) {
        this.longDateFormat = longDateFormat;
    }
}
