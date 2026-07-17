package com.edatasite.workforce.gwt.core.server.controllers.login;

/**
 * User: Aziz
 * Date: 2/24/14
 */
public class OAuthUser {

    String id;
    String firstName;
    String lastName;
    String email;
    String linkedinToken;
    String linkedinCompany;
    String facebookToken;

    public OAuthUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLinkedinToken() {
        return linkedinToken;
    }

    public void setLinkedinToken(String linkedinToken) {
        this.linkedinToken = linkedinToken;
    }

    public String getLinkedinCompany() {
        return linkedinCompany;
    }

    public void setLinkedinCompany(String linkedinCompany) {
        this.linkedinCompany = linkedinCompany;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }
}
