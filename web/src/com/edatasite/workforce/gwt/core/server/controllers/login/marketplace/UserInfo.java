package com.edatasite.workforce.gwt.core.server.controllers.login.marketplace;

/**
 * User: Anvarbek
 * Date: May 6, 2010
 * Time: 7:41:35 PM
 */

import java.io.Serial;
import java.io.Serializable;

/**
 * Simple representation of an authenticated user.
 */
public class UserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String claimedId;
    private String email;
    private String firstName;
    private String lastName;
    private String country;
    private String domain;
    private String marketplaceSection;
    private String userNameBeforeAt;
    private String company;
    private String picture;

    public UserInfo() {
    }

    public UserInfo(String claimedId, String email) {
        this.claimedId = claimedId;
        this.email = email;
    }

    public UserInfo(String claimedId, String email, String firstName, String lastName) {
        this.claimedId = claimedId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserInfo(String claimedId, String email, String firstName, String lastName, String country) {
        this(claimedId, email, firstName, lastName);
        this.country = country;
    }


    public String getClaimedId() {
        return claimedId;
    }

    public String getEmail() {
        return email;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUserNameBeforeAt() {
        return userNameBeforeAt;
    }

    public void setUserNameBeforeAt(String userNameBeforeAt) {
        this.userNameBeforeAt = userNameBeforeAt;
    }

    public String getMarketplaceSection() {
        return marketplaceSection;
    }

    public void setMarketplaceSection(String marketplaceSection) {
        this.marketplaceSection = marketplaceSection;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "\nEmail:" + this.email + ";\nFirstName:" + this.firstName + ";\n" +
                "LirstName:" + this.lastName + ";\nCountry:" + this.country + ";\n" +
                "Domain:" + this.domain + ";\nUserNameBeforeAt:" + this.userNameBeforeAt;
    }

}
